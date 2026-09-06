package net.chamosmp.chamoparty.paper.votestorage.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import net.chamosmp.chamoparty.save.RedisConfiguration;

public class ChamoRedisClient {

    private final RedisClient redisClient;
    private final StatefulRedisPubSubConnection<String, String> connection;
    private final RedisPubSubAsyncCommands<String, String> asyncCommands;

    public ChamoRedisClient() {
        RedisConfiguration config = LegacyJsonConfig.redis;
        redisClient = config.password() != null
                ? RedisClient.create("redis://" + config.password() + "@" + config.host() + ":" + config.port() + "/0")
                : RedisClient.create("redis://" + config.host() + ":" + config.port() + "/0");


        connection = redisClient.connectPubSub();
        asyncCommands = connection.async();
    }

    public StatefulRedisPubSubConnection<String, String> getConnection() {
        return connection;
    }

    public RedisPubSubAsyncCommands<String, String> getAsyncCommands() {
        return asyncCommands;
    }

    public void close() {
        asyncCommands.shutdown(true);
        connection.close();
        redisClient.shutdown();
    }
}
