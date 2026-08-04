package net.chamosmp.chamoparty.api.storage;

public enum Storage {

    MYSQL("jdbc:mysql://"),

    MARIADB("jdbc:mariadb://"),
    PGSQL("jdbc:postgresql://"),
    SQLITE(""),
    JSON,

    REDIS,

    ;

    private final String urlBase;

    /**
     * @param urlBase
     */
    Storage(String urlBase) {
        this.urlBase = urlBase;
    }

    Storage() {
        this(null);
    }

    public String getUrlBase() {
        return urlBase;
    }

}
