package net.chamosmp.chamoparty.core.enums;

public enum Folder {

    PLAYERS,

    ;


    public String toFolder() {
        return name().toLowerCase();
    }

}
