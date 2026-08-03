package net.chamosmp.chamoparty.paper.core.utils;

import net.chamosmp.chamoparty.core.utils.ColorUtils;
import net.chamosmp.chamoparty.paper.placeholder.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PapiUtils {

    private transient boolean usePlaceHolder;

    public PapiUtils() {
        usePlaceHolder = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    /**
     *
     * @param placeHolder
     * @param player
     * @return string
     */
    public String papi(String placeHolder, Player player) {

        if (placeHolder == null)
            return null;

        if (!usePlaceHolder)
            usePlaceHolder = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        if (usePlaceHolder) {
            return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, placeHolder);
        } else
            return PlaceholderAPI.getInstance().setPlaceholders(player, placeHolder);
    }

    /**
     *
     * @param placeHolder
     * @param player
     * @return string
     */
    public Component papi(Component message, Player player) {
        if (message == null)
            return null;

        if (!usePlaceHolder)
            usePlaceHolder = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        if (usePlaceHolder) {
            return ColorUtils.parse(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, ColorUtils.deParse(message)));
        } else
            return ColorUtils.parse(PlaceholderAPI.getInstance().setPlaceholders(player, ColorUtils.deParse(message)));
    }

}