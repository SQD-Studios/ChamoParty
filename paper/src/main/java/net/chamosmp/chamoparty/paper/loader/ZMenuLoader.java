package net.chamosmp.chamoparty.paper.loader;

import fr.maxlego08.menu.api.Inventory;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.exceptions.InventoryException;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.utils.MessageUtils;
import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import net.chamosmp.sqdlib.util.LogType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.Optional;

public class ZMenuLoader {

    private final ChamoPartyPlugin plugin;
    private InventoryManager inventoryManager;

    public ZMenuLoader(ChamoPartyPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        RegisteredServiceProvider<InventoryManager> provider = Bukkit.getServer().getServicesManager().getRegistration(InventoryManager.class);
        if (provider == null) {
            LoggerUtil.log(LogType.WARNING, "Unable to retrieve the provider InventoryManager!");
            this.inventoryManager = null;
            return;
        }
        this.inventoryManager = provider.getProvider();
    }

    public void reload() {
        if (this.inventoryManager == null) {
            LoggerUtil.log(LogType.WARNING, "Skipping inventories as zMenu is not available.");
            return;
        }

        File file = new File(this.plugin.getDataFolder(), "inventories/vote.yml");
        try {
            this.inventoryManager.deleteInventories(this.plugin);
            this.inventoryManager.loadInventory(this.plugin, file);
        } catch (InventoryException e) {
            e.printStackTrace();
        }
    }

    public void open(Player player) {
        Optional<Inventory> optional = this.inventoryManager.getInventory("vote");
        if (optional.isPresent()) {
            Inventory inventory = optional.get();
            this.inventoryManager.openInventory(player, inventory);
        } else
            MessageUtils.message(player, "<red>Error with the inventories!");
    }

    public boolean isLoaded() {
        return this.inventoryManager != null;
    }
}