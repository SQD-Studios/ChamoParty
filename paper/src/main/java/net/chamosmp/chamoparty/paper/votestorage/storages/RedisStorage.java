package net.chamosmp.chamoparty.paper.votestorage.storages;

import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.votestorage.redis.ChamoRedisClient;
import net.chamosmp.chamoparty.paper.votestorage.redis.RedisPubSub;


public class RedisStorage extends RemoteSqlStorage implements IStorage {

    private final ChamoRedisClient redisClient;
    private final RedisPubSub messaging;

    /**
     * @param storage
     * @param plugin
     */
    public RedisStorage(Storage storage, ChamoPartyPlugin plugin) {
        super(plugin, storage);
        this.redisClient = new ChamoRedisClient();
        this.messaging = new RedisPubSub(plugin, this, this.redisClient);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void save() {
        super.save();
        try {
            this.messaging.stop();
        } catch (Exception _) {
        }
    }

    /**
     * Add vote count but it's a secret
     *
     * @param i
     */
    public void addSecretVoteCount(int i) {
        this.voteCount += i;
    }

    @Override
    public void addVoteCount(long amount) {
        super.addVoteCount(amount);
        this.messaging.sendAddVoteCount();
    }

    @Override
    public void startVoteParty() {
        super.startVoteParty();
        this.messaging.sendHandleVoteParty();
    }

    public void setSecretVoteCount(int i) {
        this.voteCount = 0;
    }

}
