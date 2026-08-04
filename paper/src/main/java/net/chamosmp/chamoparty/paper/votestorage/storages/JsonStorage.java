package net.chamosmp.chamoparty.paper.votestorage.storages;

import net.chamosmp.chamoparty.core.enums.Folder;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.api.Vote;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.core.Plugin;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.utils.storage.Persist;
import net.chamosmp.chamoparty.paper.implementations.ChamoPlayerVote;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class JsonStorage implements IStorage {

    private transient final Plugin plugin;
    private transient final Map<UUID, PlayerVote> players = new HashMap<>();

    private long voteCount = 1;

    /**
     * @param plugin
     */
    public JsonStorage(Plugin plugin) {
        super();
        this.plugin = plugin;
    }

    /**
     *
     * @param uniqueId
     * @return Optional of PlayerVote
     */
    private Optional<PlayerVote> getPlayer(UUID uniqueId) {

        if (this.players.containsKey(uniqueId))
            return Optional.of(this.players.get(uniqueId));

        String userFile = Folder.PLAYERS.toFolder() + "/" + uniqueId + ".json";
        File file = new File(plugin.getDataFolder(), userFile);
        if (file.exists()) {
            try {
                PlayerVote playerVote = this.plugin.getPersist().loadOrSaveDefault(null, ChamoPlayerVote.class,
                        Folder.PLAYERS, uniqueId.toString());
                players.put(uniqueId, playerVote);
                return Optional.of(playerVote);
            } catch (Exception e) {
                if (LegacyJsonConfig.enableDebug) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public PlayerVote createPlayer(OfflinePlayer offlinePlayer) {
        return this.createPlayer(offlinePlayer.getUniqueId());
    }

    @Override
    public PlayerVote createPlayer(UUID uuid) {
        PlayerVote playerVote = new ChamoPlayerVote(uuid);
        players.put(uuid, playerVote);
        this.plugin.getPersist().save(playerVote, Folder.PLAYERS, playerVote.getFileName());
        return playerVote;
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
    }

    @Override
    public void setVoteCount(long amount) {
        this.voteCount = amount;
    }

    @Override
    public void save(Persist persist) {

    }

    @Override
    public void load(Persist persist) {

    }

    @Override
    public void getPlayer(OfflinePlayer offlinePlayer, Consumer<Optional<PlayerVote>> consumer, boolean forceDatabaseUpdate) {
        consumer.accept(this.getPlayer(offlinePlayer.getUniqueId()));
    }

    @Override
    public void getPlayer(UUID uuid, Consumer<Optional<PlayerVote>> consumer, boolean forceDatabaseUpdate) {
        consumer.accept(this.getPlayer(uuid));
    }

    @Override
    public Optional<PlayerVote> getSyncPlayer(Player player) {
        return player != null ? this.getPlayer(player.getUniqueId()) : Optional.empty();
    }

    @Override
    public void insertVote(PlayerVote playerVote, Vote vote, Reward reward) {

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
    public void createPlayer(PlayerVote playerVote) {
        this.players.put(playerVote.getUniqueId(), playerVote);
    }

    @Override
    public void updateRewards(UUID uniqueId) {
        this.getPlayer(uniqueId, optional -> {
            optional.ifPresent(playerVote -> this.plugin.getPersist().save(playerVote, Folder.PLAYERS, playerVote.getFileName()));
        }, false);
    }

}