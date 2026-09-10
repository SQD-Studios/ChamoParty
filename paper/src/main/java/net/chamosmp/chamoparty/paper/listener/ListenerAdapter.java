package net.chamosmp.chamoparty.paper.listener;

import net.chamosmp.chamoparty.paper.core.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public abstract class ListenerAdapter extends Utils {
    protected void onConnect(PlayerJoinEvent event, Player player) {
    }
}