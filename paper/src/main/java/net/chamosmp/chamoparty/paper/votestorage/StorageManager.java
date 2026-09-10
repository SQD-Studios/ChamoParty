package net.chamosmp.chamoparty.paper.votestorage;

import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import net.chamosmp.chamoparty.paper.votestorage.storages.RedisStorage;
import net.chamosmp.chamoparty.paper.votestorage.storages.RemoteSqlStorage;
import net.chamosmp.chamoparty.paper.votestorage.storages.SqliteStorage;

public class StorageManager implements net.chamosmp.chamoparty.paper.api.storage.StorageManager {

    private final net.chamosmp.chamoparty.api.storage.Storage storage;
    private IStorage iStorage;

    /**
     * @param storage
     * @param plugin
     */
    public StorageManager(net.chamosmp.chamoparty.api.storage.Storage storage, ChamoPartyPlugin plugin) {
        super();
        this.storage = storage;

        if (storage == null) {
            return;
        }
        switch (storage) {
            case MYSQL, MARIADB:
                this.iStorage = new RemoteSqlStorage(plugin, storage);
                break;
            case REDIS:
                this.iStorage = new RedisStorage(LegacyJsonConfig.redisSqlStorage, plugin);
                break;
            case SQLITE:
                this.iStorage = new SqliteStorage(plugin);
        }
    }

    @Override
    public void save() {
        switch (this.storage) {
            case REDIS, SQLITE:
                this.iStorage.save();
                break;
        }
    }

    @Override
    public void load() {
        if (this.storage == null) {
            return;
        }
        switch (this.storage) {
            case REDIS, SQLITE:
                this.iStorage.load();
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
