package net.chamosmp.chamoparty.save;

public class RedisConfiguration {
    private final String host;
    private final int port;
    private final String password;
    private final int databaseIndex;

    /**
     * @param host
     * @param port
     * @param password
     * @param databaseIndex
     * @param poolConfig
     */
    public RedisConfiguration(String host, int port, String password, int databaseIndex,
                              RedisPoolConfiguration poolConfig) {
        super();
        this.host = host;
        this.port = port;
        this.password = password;
        this.databaseIndex = databaseIndex;
        this.poolConfig = poolConfig;
    }

    private final RedisPoolConfiguration poolConfig;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public RedisPoolConfiguration getPoolConfig() {
        return poolConfig;
    }

    public int getDatabaseIndex() {
        return databaseIndex;
    }

    public record RedisPoolConfiguration(int maxTotal, int maxIdle, int minIdle) {
        /**
         * @param maxTotal
         * @param maxIdle
         * @param minIdle
         */
        public RedisPoolConfiguration {
        }
        }
}
