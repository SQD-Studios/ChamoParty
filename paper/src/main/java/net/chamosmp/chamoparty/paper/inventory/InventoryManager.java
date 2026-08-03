package net.chamosmp.chamoparty.paper.inventory;

import net.chamosmp.chamoparty.api.enums.Message;
import net.chamosmp.chamoparty.core.enums.EnumInventory;
import net.chamosmp.chamoparty.core.utils.inventory.InventoryResult;
import net.chamosmp.chamoparty.exceptions.InventoryAlreadyExistException;
import net.chamosmp.chamoparty.exceptions.InventoryOpenException;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.utils.inventory.ItemButton;
import net.chamosmp.chamoparty.paper.listener.ListenerAdapter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager extends ListenerAdapter {

    private final Map<Integer, Inventory> inventories = new HashMap<>();
    private final Map<UUID, Inventory> playerInventories = new HashMap<>();
    private final ChamoPartyPlugin plugin;

    /**
     * @param plugin
     */
    public InventoryManager(ChamoPartyPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    public void sendLog() {
        Logger.log("Loading " + inventories.size() + " inventories", LogType.SUCCESS);
    }

    /**
     * Register new inventory
     *
     * @param inv
     * @param inventory
     */
    public void registerInventory(EnumInventory inv, Inventory inventory) {
        if (!inventories.containsKey(inv.getId()))
            inventories.put(inv.getId(), inventory);
        else
            throw new InventoryAlreadyExistException("Inventory with id " + inv.getId() + " already exist !");
    }

    /**
     *
     * @param inv
     * @param player
     * @param page
     * @param objects
     */
    public void createInventory(EnumInventory inv, Player player, int page, Object... objects) throws CloneNotSupportedException {
        this.createInventory(inv.getId(), player, page, objects);
    }

    /**
     *
     * @param id
     * @param player
     * @param page
     * @param objects
     */
    public void createInventory(int id, Player player, int page, Object... objects) throws CloneNotSupportedException {
        Inventory inventory = getInventory(id);
        if (inventory == null) {
            message(player, Message.INVENTORY_CLONE_NULL, "%id%", id);
            return;
        }
        Inventory clonedInventory = inventory.clone();

        if (clonedInventory == null) {
            message(player, Message.INVENTORY_CLONE_NULL);
            return;
        }

        clonedInventory.setId(id);
        try {
            InventoryResult result = clonedInventory.preOpenInventory(plugin, player, page, objects);
            if (result.equals(InventoryResult.SUCCESS)) {
                player.openInventory(clonedInventory.getInventory());
                playerInventories.put(player.getUniqueId(), clonedInventory);
            } else if (result.equals(InventoryResult.ERROR))
                message(player, Message.INVENTORY_OPEN_ERROR, "%id%", id);
        } catch (InventoryOpenException e) {
            message(player, Message.INVENTORY_OPEN_ERROR, "%id%", id);
            e.printStackTrace();
        }
    }

    @Override
    protected void onInventoryClick(InventoryClickEvent event, Player player) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getWhoClicked() instanceof Player) {
            if (!exist(player))
                return;
            Inventory gui = playerInventories.get(player.getUniqueId());
            if (gui.getGuiComponent() == null) {
                Logger.log("An error has occurred with the menu ! " + gui.getClass().getName());
                return;
            }
            if (gui.getPlayer().equals(player) && event.getView().title().equals(gui.getGuiComponent())) {
                event.setCancelled(true);
                ItemButton button = gui.getItems().getOrDefault(event.getSlot(), null);
                if (button != null)
                    button.onClick(event);
            }
        }
    }

    @Override
    protected void onInventoryClose(InventoryCloseEvent event, Player player) {
        if (!exist(player))
            return;
        Inventory inventory = playerInventories.get(player.getUniqueId());
        remove(player);
        inventory.onClose(event, plugin, player);
    }

    @Override
    protected void onInventoryDrag(InventoryDragEvent event, Player player) {
        if (event.getWhoClicked() instanceof Player) {
            if (!exist(player))
                return;
            playerInventories.get(player.getUniqueId()).onDrag(event, plugin, player);
        }
    }

    public boolean exist(Player player) {
        return playerInventories.containsKey(player.getUniqueId());
    }

    public void remove(Player player) {
        playerInventories.remove(player.getUniqueId());
    }

    private Inventory getInventory(int id) {
        return inventories.getOrDefault(id, null);
    }

}