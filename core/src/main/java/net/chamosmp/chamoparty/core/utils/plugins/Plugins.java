package net.chamosmp.chamoparty.core.utils.plugins;

public enum Plugins {
    
    PLACEHOLDER("PlaceholderAPI"),
    VOTIFIER("Votifier"),
    ZMENU("zMenu"),

    ;

    private final String name;

    Plugins(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

}
