package net.chamosmp.chamoparty.core.enums;

public enum EnumInventory {

    INVENTORY_DEFAULT(1),
    INVENTORY_CONFIG(2),

    ;

    private final int id;

    EnumInventory(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
