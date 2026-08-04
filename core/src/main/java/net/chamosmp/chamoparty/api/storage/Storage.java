package net.chamosmp.chamoparty.api.storage;

public enum Storage {

    MYSQL(Type.REDIS_SQL, "jdbc:mysql://"),

    MARIADB(Type.REDIS_SQL, "jdbc:mariadb://"),
    PGSQL(Type.REDIS_SQL, "jdbc:postgresql://"),
    SQLITE(Type.REDIS_SQL, ""),

    JSON(Type.VOTE_STORING),
    REDIS(Type.VOTE_STORING),

    ;

    private final String urlBase;
    private final Type type;

    Storage(Type storageType, String urlBase) {
        this.urlBase = urlBase;
        this.type = storageType;
    }

    Storage(Type type) {
        this.urlBase = null;
        this.type = type;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        REDIS_SQL,
        VOTE_STORING
    }
}
