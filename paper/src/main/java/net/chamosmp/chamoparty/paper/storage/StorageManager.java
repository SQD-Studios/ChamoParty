package net.chamosmp.chamoparty.paper.storage;

import net.chamosmp.chamoparty.core.enums.Folder;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.core.utils.storage.Persist;
import net.chamosmp.chamoparty.paper.save.Config;
import net.chamosmp.chamoparty.paper.save.VoteStorage;
import net.chamosmp.chamoparty.paper.storage.storages.JsonStorage;
import net.chamosmp.chamoparty.paper.storage.storages.RedisStorage;
import net.chamosmp.chamoparty.paper.storage.storages.SqlStorage;

public class StorageManager implements net.chamosmp.chamoparty.paper.api.storage.StorageManager {

    private final net.chamosmp.chamoparty.api.storage.Storage storage;
    private final ChamoPartyPlugin plugin;
    private IStorage iStorage;

    /**
     * @param storage
     * @param plugin
     */
    public StorageManager(net.chamosmp.chamoparty.api.storage.Storage storage, ChamoPartyPlugin plugin) {
        super();
        this.storage = storage;
        this.plugin = plugin;

        switch (storage) {
            case JSON:
                this.iStorage = new JsonStorage(plugin);
                break;
            case MYSQL:
            case SQLITE:
            case PGSQL:
            case MARIADB:
                this.iStorage = new SqlStorage(plugin, storage);
                break;
            case REDIS:
                this.iStorage = new RedisStorage(Config.redisSqlStorage, plugin);
                break;
            default:
                break;
        }
    }

    @Override
    public void save(Persist persist) {
        switch (this.storage) {
            case JSON:
                VoteStorage.voteCount = this.iStorage.getVoteCount();
                VoteStorage.getInstance().save(this.plugin.getPersist());
                this.iStorage.getPlayers().forEach((uuid, player) -> {
                    if (player != null) {
                        persist.save(player, Folder.PLAYERS, player.getFileName());
                    }
                });
                break;
            case MYSQL:
            case SQLITE:
            case PGSQL:
            case MARIADB:
            case REDIS:
                this.iStorage.save(persist);
                break;
            default:
                break;
        }
    }

    @Override
    public void load(Persist persist) {
        switch (this.storage) {
            case JSON:
                VoteStorage.getInstance().load(this.plugin.getPersist());
                this.iStorage.setVoteCount(VoteStorage.voteCount);
            case MYSQL:
            case SQLITE:
            case PGSQL:
            case MARIADB:
            case REDIS:
                this.iStorage.load(persist);
                break;
            default:
                break;
        }
    }

    @Override
    public net.chamosmp.chamoparty.api.storage.Storage getStorage() {
        return this.storage;
    }

    @Override
    public IStorage getIStorage() {
        return this.iStorage;
    }

}
