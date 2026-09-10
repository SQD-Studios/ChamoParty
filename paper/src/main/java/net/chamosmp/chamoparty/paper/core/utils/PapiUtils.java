package net.chamosmp.chamoparty.paper.core.utils;

import net.chamosmp.chamoparty.paper.placeholder.PlaceholderAPI;
import net.chamosmp.sqdlib.paper.util.ColorUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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
     * Parse a message with Placeholders
     *
     * @param message The message
     * @param player  The player
     * @return The colored message
     */
    public Component papi(Component message, @Nullable Player player) {
        if (message == null)
            return null;

        if (!usePlaceHolder)
            usePlaceHolder = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        if (usePlaceHolder) {
            return ColorUtil.parse(player, ColorUtil.deParse(message));
        } else
            return ColorUtil.parse(PlaceholderAPI.getInstance().setPlaceholders(player, ColorUtil.deParse(message)));
    }

}