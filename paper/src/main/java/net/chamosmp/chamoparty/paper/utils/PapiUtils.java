package net.chamosmp.chamoparty.paper.utils;

import net.chamosmp.chamoparty.paper.placeholder.PlaceholderAPI;
import net.chamosmp.sqdlib.paper.util.ColorUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PapiUtils {

    private final static boolean IS_PAPI_ENABLED = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

    /**
     *
     * @param placeHolder
     * @param player
     * @return string
     */
    public String papi(String placeHolder, Player player) {
        if (placeHolder == null)
            return null;

        if (IS_PAPI_ENABLED) {
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

        if (IS_PAPI_ENABLED) {
            return ColorUtil.parse(player, ColorUtil.deParse(message));
        } else
            return ColorUtil.parse(PlaceholderAPI.getInstance().setPlaceholders(player, ColorUtil.deParse(message)));
    }
}