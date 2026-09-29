package net.chamosmp.chamoparty.paper.database.storages;

import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.api.Vote;
import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.database.utils.SqlConnection;
import net.chamosmp.chamoparty.paper.impl.ChamoPlayerVote;
import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import net.chamosmp.sqdlib.paper.util.SchedulerUtil;
import net.chamosmp.sqdlib.util.log.LogType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class SqliteStorage implements IStorage {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger("ChamoParty");
    protected final ChamoPartyPlugin plugin;
    protected final Storage storage;
    protected transient final Map<UUID, PlayerVote> players = new HashMap<>();
    protected IConnection iConnection;
    protected long voteCount = 1;

    /**
     * @param plugin the plugin
     */
    public SqliteStorage(ChamoPartyPlugin plugin) {

        this.plugin = plugin;
        this.storage = Storage.SQLITE;
    }

    @Override
    public void load() {
        this.iConnection = new SqlConnection(storage, plugin);

        LoggerUtil.log(LogType.INFO, "Connecting to the SQL database... (SQLite)");
        SchedulerUtil.runAsync(plugin, () -> {
            try {
                this.iConnection.connect();
                log.info("Connected to the SQL database");
            } catch (SQLException e) {
                log.error("Could not connect to the SQL database: ", e);
                return;
            }

            try (java.sql.Connection conn = iConnection.getConnection()) {
                conn.createStatement().execute("""
                        CREATE TABLE IF NOT EXISTS chamoparty_count (
                            vote BIGINT NOT NULL
                        )
                        """);
                conn.createStatement().execute("""
                        CREATE TABLE IF NOT EXISTS chamoparty_votes (
                            player_uuid             varchar(36)          not null,
                            service_name   varchar(255)         not null,
                            is_reward_give boolean default true not null,
                            reward_percent float   default 100  not null,
                            commands       longtext             not null,
                            need_online    boolean default true not null,
                            created_at     long    				not null
                        )
                        """);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            log.info("Successfully run pre-database sql");

            this.iConnection.fetchVotes(this);

        });

    }

    @Override
    public void save() {
    }

    @Override
    public PlayerVote createPlayer(OfflinePlayer offlinePlayer) {
        return this.createPlayer(offlinePlayer.getUniqueId());
    }

    @Override
    public long getVoteCount() {
        return this.voteCount;
    }

    @Override
    public void setVoteCount(long amount) {
        this.voteCount = amount;
        this.iConnection.updateVoteCount(this.voteCount);
    }

    @Override
    public void addVoteCount(long amount) {
        this.voteCount += amount;
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