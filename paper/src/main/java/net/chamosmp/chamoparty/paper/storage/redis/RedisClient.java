package net.chamosmp.chamoparty.paper.storage.redis;

import net.chamosmp.chamoparty.paper.save.Config;
import net.chamosmp.chamoparty.save.RedisConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisClient {

    private final JedisPool jedis;

    public RedisClient() {
        RedisConfiguration config = Config.redis;

        this.jedis = config.getPassword() != null
                ? new JedisPool(buildPoolConfig(config), config.getHost(), config.getPort(), Protocol.DEFAULT_TIMEOUT,
                config.getPassword())
                : new JedisPool(buildPoolConfig(config), config.getHost(), config.getPort());

    }

    public Jedis getPool() {
        Jedis jedis = this.jedis.getResource();

        RedisConfiguration config = Config.redis;
        if (config.getDatabaseIndex() != 0) {
            jedis.select(config.getDatabaseIndex());
        }

        return jedis;
    }

    private JedisPoolConfig buildPoolConfig(RedisConfiguration config) {
        RedisConfiguration.RedisPoolConfiguration poolConfig = config.getPoolConfig();
        JedisPoolConfig poolConfigBuilder = new JedisPoolConfig();

        poolConfigBuilder.setMaxTotal(poolConfig.maxTotal());
        poolConfigBuilder.setMaxIdle(poolConfig.maxIdle());
        poolConfigBuilder.setMinIdle(poolConfig.minIdle());

        return poolConfigBuilder;
    }
}
