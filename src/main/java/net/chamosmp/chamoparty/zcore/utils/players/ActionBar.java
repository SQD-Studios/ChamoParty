package net.chamosmp.chamoparty.zcore.utils.players;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.chamosmp.chamoparty.zcore.utils.nms.NMSUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar {
	private static String nmsver;
	private static boolean useOldMethods = false;
	private static double nmsVersion = NMSUtils.version	;

	public static void sendActionBar(Player player, String message) {
		if (!player.isOnline()) return;
		Component component = MiniMessage.miniMessage().deserialize(message);
		player.sendActionBar(component);
	}

	public static void sendActionBar(Player player, String message, int sec) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			int currentSec = sec;

			@Override
			public void run() {
				if (currentSec <= 0) {
					cancel();
					return;
				}
				Component component = MiniMessage.miniMessage().deserialize(message);
				player.sendActionBar(component);
				currentSec--;
			}
		}, 0, 1000);
	}

	public static void broadcastActionMessage(String paramString) {
		broadcastActionMessage(paramString, -1);
	}

	public static void broadcastActionMessage(String paramString, int timer) {
		for (Player localPlayer : Bukkit.getOnlinePlayers())
			sendActionBar(localPlayer, paramString);
	}
}
