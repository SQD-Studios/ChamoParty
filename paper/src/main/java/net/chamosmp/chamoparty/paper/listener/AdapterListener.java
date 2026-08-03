package net.chamosmp.chamoparty.paper.listener;

import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AdapterListener extends Utils implements Listener {

    private final ChamoPartyPlugin template;

    public AdapterListener(ChamoPartyPlugin template) {
        this.template = template;
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        template.getListenerAdapters().forEach(adapter -> adapter.onConnect(event, event.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        template.getListenerAdapters().forEach(adapter -> adapter.onQuit(event, event.getPlayer()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        template.getListenerAdapters()
                .forEach(adapter -> adapter.onInventoryClick(event, (Player) event.getWhoClicked()));
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        template.getListenerAdapters()
                .forEach(adapter -> adapter.onInventoryDrag(event, (Player) event.getWhoClicked()));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        template.getListenerAdapters().forEach(adapter -> adapter.onInventoryClose(event, (Player) event.getPlayer()));
    }
}
