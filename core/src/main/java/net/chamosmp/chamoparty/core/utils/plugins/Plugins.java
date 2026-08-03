package net.chamosmp.chamoparty.core.utils.plugins;

public enum Plugins {

    VAULT("Vault"),
    ESSENTIALS("Essentials"),
    HEADDATABASE("HeadDatabase"),
    PLACEHOLDER("PlaceholderAPI"),
    CITIZENS("Citizens"),
    TRANSLATIONAPI("TranslationAPI"),
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
