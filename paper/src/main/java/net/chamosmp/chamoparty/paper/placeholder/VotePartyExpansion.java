package net.chamosmp.chamoparty.paper.placeholder;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class VotePartyExpansion extends PlaceholderExpansion {

	private final Plugin plugin;

	/**
	 * @param plugin
	 */
	public VotePartyExpansion(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public String getAuthor() {
		return "Chamogelastos";
	}

	@Override
	public String getIdentifier() {
		return "chamoparty";
	}

	@Override
	public String getVersion() {
		return plugin.getPluginMeta().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String params) {
		return PlaceholderAPI.getInstance().onRequest(player, params);
	}

	@Override
	public boolean persist() {
		return true;
	}
	
}
