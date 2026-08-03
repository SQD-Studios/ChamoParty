package net.chamosmp.chamoparty.paper.loader;

import fr.maxlego08.menu.api.Inventory;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.exceptions.InventoryException;
import net.chamosmp.chamoparty.api.enums.InventoryName;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Optional;

public class ZMenuLoader extends Utils {

    private InventoryManager inventoryManager;

    private final ChamoPartyPlugin plugin;

    /**
     * @param plugin
     */
    public ZMenuLoader(ChamoPartyPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    public void load() {
        this.inventoryManager = plugin.getProvider(InventoryManager.class);
    }

    public void reload() {
        if (this.inventoryManager == null) {
            this.plugin.getLogger().warning("Skipping inventories as zMenu is not available.");
            return;
        }

        File file = new File(this.plugin.getDataFolder(), "inventories/" + InventoryName.VOTE.getName() + ".yml");
        try {
            this.inventoryManager.deleteInventories(this.plugin);
            this.inventoryManager.loadInventory(this.plugin, file);
        } catch (InventoryException e) {
            e.printStackTrace();
        }
    }

    public void open(Player player) {
        Optional<Inventory> optional = this.inventoryManager.getInventory(InventoryName.VOTE.getName());
        if (optional.isPresent()) {
            Inventory inventory = optional.get();
            this.inventoryManager.openInventory(player, inventory);
        } else
            message(player, "<red>Error with the inventories!");
    }

    public boolean isLoaded() {
        return this.inventoryManager != null;
    }

}
