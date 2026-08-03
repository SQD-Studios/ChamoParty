package net.chamosmp.chamoparty.paper.core.utils.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class ItemButton {

    private Consumer<InventoryClickEvent> onClick;
    private final int slot;

    public ItemButton(int slot) {
        super();
        this.slot = slot;
    }

    public void setClick(Consumer<InventoryClickEvent> onClick) {
        this.onClick = onClick;
    }

    /**
     * Permet de gérer le click du joueur
     *
     * @param event
     */
    public void onClick(InventoryClickEvent event) {
        if (onClick != null)
            onClick.accept(event);
    }

    public int getSlot() {
        return this.slot;
    }

}
