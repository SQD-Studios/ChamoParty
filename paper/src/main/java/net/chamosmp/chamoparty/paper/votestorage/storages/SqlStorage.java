package net.chamosmp.chamoparty.paper.votestorage.storages;

import net.chamosmp.chamoparty.api.storage.Script;
import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.core.enums.Folder;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.api.Vote;
import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.sched.SchedulerUtil;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import net.chamosmp.chamoparty.paper.core.utils.storage.Persist;
import net.chamosmp.chamoparty.paper.implementations.ChamoPlayerVote;
import net.chamosmp.chamoparty.paper.votestorage.utils.Connection;
import net.chamosmp.chamoparty.storage.utils.ScriptRunner;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class SqlStorage extends Utils implements IStorage {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger("ChamoParty");
    protected final ChamoPartyPlugin plugin;
    protected final Storage storage;

    protected IConnection iConnection;

    protected transient final Map<UUID, PlayerVote> players = new HashMap<>();
    protected long voteCount = 1;

    /**
     * @param plugin
     * @param storage
     */
    public SqlStorage(ChamoPartyPlugin plugin, Storage storage) {
        super();
        this.plugin = plugin;
        this.storage = storage;
    }

    @Override
    public void load(Persist persist) {
        String user = plugin.getConfig().getString("database.redis.sql.sql-credentials.user");
        String password = plugin.getConfig().getString("database.redis.sql.sql-credentials.password");
        String host = plugin.getConfig().getString("database.redis.sql.sql-credentials.host");
        String database = plugin.getConfig().getString("database.redis.sql.sql-credentials.database");
        int port = plugin.getConfig().getInt("database.redis.sql.sql-credentials.port");

        this.iConnection = new Connection(storage, user, password, host, database, port);

        Logger.log("Connecting to database... (MySQL/MariaDB)");
        SchedulerUtil.runAsync(plugin, () -> {
            try {
                this.iConnection.connect();
                log.info("Connected to database");
            } catch (SQLException e) {
                log.error("Could not connect to database: ", e);
                return;
            }

            try {

                for (Script script : Script.values()) {
                    File file = new File(plugin.getDataFolder(), "scripts/" + script.name().toLowerCase() + ".sql");
                    ScriptRunner runner = new ScriptRunner(iConnection.getConnection());
                    Reader reader = new BufferedReader(new FileReader(file));
                    runner.runScript(reader);
                    reader.close();
                }
                log.info("Successfully run all the scripts");

                this.iConnection.fetchVotes(this);

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    @Override
    public void save(Persist persist) {
        // TODO Auto-generated method stub

    }

    @Override
    public PlayerVote createPlayer(OfflinePlayer offlinePlayer) {
        return this.createPlayer(offlinePlayer.getUniqueId());
    }

    @Override
    public File getFolder() {
        return new File(this.plugin.getDataFolder(), Folder.PLAYERS.toFolder());
    }

    @Override
    public Map<UUID, PlayerVote> getPlayers() {
        return this.players;
    }

    @Override
    public long getVoteCount() {
        return this.voteCount;
    }

    @Override
    public void addVoteCount(long amount) {
        this.voteCount += amount;
        this.iConnection.updateVoteCount(this.voteCount);
    }

    @Override
    public void setVoteCount(long amount) {
        this.voteCount = amount;
        this.iConnection.updateVoteCount(this.voteCount);
    }

    @Override
    public void getPlayer(OfflinePlayer offlinePlayer, Consumer<Optional<PlayerVote>> consumer, boolean forceDatabaseUpdate) {
        this.getPlayer(offlinePlayer.getUniqueId(), consumer, forceDatabaseUpdate);
    }

    @Override
    public Optional<PlayerVote> getSyncPlayer(Player player) {
        if (this.players.containsKey(player.getUniqueId())) {
            return Optional.of(this.players.get(player.getUniqueId()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void insertVote(PlayerVote playerVote, Vote vote, Reward reward) {
        this.iConnection.asyncInsert(playerVote, vote, reward);
    }

    @Override
    public void performCustomVoteAction(String username, String serviceName, UUID uuid) {
        Logger.log("Impossible to find the player " + username, LogType.WARNING);
    }

    @Override
    public void startVoteParty() {
        this.setVoteCount(0);
    }

    @Override
    public void getPlayer(UUID uuid, Consumer<Optional<PlayerVote>> consumer, boolean forceDatabaseUpdate) {
        if (this.players.containsKey(uuid) && !forceDatabaseUpdate) {
            consumer.accept(Optional.of(this.players.get(uuid)));
        } else {
            this.iConnection.asyncFetchPlayer(uuid, consumer, this);
        }
    }

    @Override
    public PlayerVote createPlayer(UUID uuid) {
        PlayerVote playerVote = new ChamoPlayerVote(uuid);
        players.put(uuid, playerVote);
        return playerVote;
    }

    @Override
    public void updateRewards(UUID uniqueId) {
        this.iConnection.updateRewards(uniqueId);
    }

    @Override
    public void createPlayer(PlayerVote playerVote) {
        this.players.put(playerVote.getUniqueId(), playerVote);
    }

}