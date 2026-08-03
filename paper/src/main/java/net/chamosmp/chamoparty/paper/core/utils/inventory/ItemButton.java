package net.chamosmp.chamoparty.paper.core.utils.inventory;

import java.util.function.Consumer;

import org.bukkit.event.inventory.InventoryClickEvent;

public class ItemButton {

	private Consumer<InventoryClickEvent> onClick;
	private final int slot;

	public ItemButton(int slot) {
		super();
		this.slot = slot;
	}

	public ItemButton setClick(Consumer<InventoryClickEvent> onClick) {
		this.onClick = onClick;
		return this;
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
