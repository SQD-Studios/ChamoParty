package net.chamosmp.chamoparty.paper.database;

import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.api.storage.StorageManager;
import net.chamosmp.chamoparty.paper.database.storages.RedisStorage;
import net.chamosmp.chamoparty.paper.database.storages.RemoteSqlStorage;
import net.chamosmp.chamoparty.paper.database.storages.SqliteStorage;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;

public class DatabaseManager implements StorageManager {

    private final Storage storage;
    private IStorage iStorage;

    /**
     * @param storage
     * @param plugin
     */
    public DatabaseManager(Storage storage, ChamoPartyPlugin plugin) {
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
    public Storage getStorage() {
        return this.storage;
    }

    @Override
    public IStorage getIStorage() {
        return this.iStorage;
    }

}
