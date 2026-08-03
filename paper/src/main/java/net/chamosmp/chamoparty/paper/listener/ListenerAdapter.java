package net.chamosmp.chamoparty.paper.listener;

import net.chamosmp.chamoparty.paper.core.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class ListenerAdapter extends Utils {

    protected void onConnect(PlayerJoinEvent event, Player player) {
    }

    protected void onQuit(PlayerQuitEvent event, Player player) {
    }

    protected void onInventoryClick(InventoryClickEvent event, Player player) {
    }

    protected void onInventoryClose(InventoryCloseEvent event, Player player) {
    }

    protected void onInventoryDrag(InventoryDragEvent event, Player player) {
    }
}
