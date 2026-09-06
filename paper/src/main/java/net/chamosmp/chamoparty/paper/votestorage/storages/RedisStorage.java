package net.chamosmp.chamoparty.paper.votestorage.storages;

import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.core.utils.storage.Persist;
import net.chamosmp.chamoparty.paper.votestorage.redis.ChamoRedisClient;
import net.chamosmp.chamoparty.paper.votestorage.redis.RedisPubSub;

import java.util.UUID;


public class RedisStorage extends SqlStorage implements IStorage {

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
    public void load(Persist persist) {
        super.load(persist);
    }

    @Override
    public void save(Persist persist) {
        super.save(persist);
        try {
            this.messaging.stop();
        } catch (Exception _) {
        }
    }

    @Override
    public void performCustomVoteAction(String username, String serviceName, UUID uuid) {
        this.messaging.sendVoteAction(username, serviceName, uuid);
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
