package net.chamosmp.chamoparty.paper.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

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
    public @NonNull String getAuthor() {
        return "Chamogelastos";
    }

    @Override
    public @NonNull String getIdentifier() {
        return "chamoparty";
    }

    @Override
    public @NonNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NonNull String params) {
        return PlaceholderAPI.getInstance().onRequest(player, params);
    }

    @Override
    public boolean persist() {
        return true;
    }

}
