package net.chamosmp.chamoparty.paper.save;

import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.core.utils.ProgressBar;
import net.chamosmp.chamoparty.save.RedisConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class LegacyJsonConfig {

    public static Storage storage = Storage.JSON;
    public static Storage redisSqlStorage = Storage.MYSQL;

    public static boolean enableDebug = false;
    public static boolean enableDebugTime = false;
    public static boolean enableLogMessage = false;

    public static boolean enableVoteInventory = true;
    public static boolean enableVoteMessage = true;

    // New flag to restrict rewards only to voters
    public static boolean only_voters_rewards = true;

    public static boolean enableActionBarVoteAnnonce = true;
    public static boolean enableTchatVoteAnnonce = false;

    public static long joinGiveVoteMilliSecond = 500;

    public static int redisServerAmount = 2;
    public static String redisChannel = "chamoparty";
    public static RedisConfiguration redis = new RedisConfiguration("192.168.10.10", 6379, null
    );
    public static int maxSqlRetryAmoun = 5;

    public static ProgressBar progressBar = new ProgressBar(20, '|', "§a", "§8");

    /**
     * static Singleton instance.
     */
    private static volatile LegacyJsonConfig instance = null;

    public LegacyJsonConfig(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();

        switch (Objects.requireNonNull(config.getString("database.type")).toLowerCase()) {
            case "json":
                storage = Storage.JSON;
                break;
            case "redis":
                storage = Storage.REDIS;
                break;
        }
        switch (Objects.requireNonNull(config.getString("database.redis.sql.sql-database")).toLowerCase()) {
            case "mysql":
                redisSqlStorage = Storage.MYSQL;
                break;
            case "mariadb":
                redisSqlStorage = Storage.MARIADB;
                break;
        }
        enableDebug = config.getBoolean("debug.enabled", false);
        enableDebugTime = config.getBoolean("debug.enabled", false);
        enableLogMessage = true;
        enableVoteInventory = config.getBoolean("vote-inventory.enabled", true);
        enableVoteMessage = true;

        enableActionBarVoteAnnonce = config.getBoolean("action-vote-announcement", true);
        enableTchatVoteAnnonce = config.getBoolean("tchat-vote-annonce", true);

        only_voters_rewards = config.getBoolean("party.only-voters-rewards", true);

        redisServerAmount = config.getInt("database.redis.redis-credentials.server-amount", 2);
        redisChannel = config.getString("database.redis.redis-credentials.channel", "chamoparty");
        redis = new RedisConfiguration(
                config.getString("database.redis.redis-credentials.host", "localhost"),
                config.getInt("database.redis.redis-credentials.port", 6379),
                config.getString("database.redis.redis-credentials.password", null)
        );
    }

    /**
     * Return a singleton instance of Config.
     */
    public static void getInstance(Plugin plugin) {
        if (instance == null) {
            synchronized (LegacyJsonConfig.class) {
                if (instance == null) {
                    instance = new LegacyJsonConfig(plugin);
                }
            }
        }
    }
}