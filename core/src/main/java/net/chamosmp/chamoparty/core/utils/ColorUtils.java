package net.chamosmp.chamoparty.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {

    public static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    /**
     * What are these weird things I do for YOU
     *
     * @param message The message to make the Legacy to minimessage
     * @return The minimessage from legacy String
     * @apiNote It's not finished yet so write it in MiniMessage to begin with
     */
    public static String legacyToMiniMessage(String message) {
        String oneChar = message.replace("§", "&");
        String twoChar = oneChar.replace("<&>", "&");
        String black = twoChar.replace("&0", "<black>");
        String dark_blue = black.replace("&1", "<dark_blue>");
        String dark_green = dark_blue.replace("&2", "<dark_green>");
        String dark_aqua = dark_green.replace("&3", "<dark_aqua>");
        String dark_red = dark_aqua.replace("&4", "<dark_red>");
        String dark_purple = dark_red.replace("&5", "<dark_purple>");
        String gold = dark_purple.replace("&6", "<gold>");
        String gray = gold.replace("&7", "<gray>");
        String dark_gray = gray.replace("&8", "<dark_gray>");
        String blue = dark_gray.replace("&9", "<blue>");
        String green = blue.replace("&a", "<green>");
        String aqua = green.replace("&b", "<aqua>");
        String red = aqua.replace("&c", "<red>");
        String light_purple = red.replace("&d", "<light_purple>");
        String yellow = light_purple.replace("&e", "<yellow>");
        String white = yellow.replace("&f", "<white>");
        String bold = white.replace("&l", "<b>");
        String italic = bold.replace("&o", "<i>");
        String underline = italic.replace("&n", "<u>");
        String strikethrough = underline.replace("&m", "<st>");

        return strikethrough.replace("&k", "<obf>");
    }

    public static Component parse(String message) {
        return MINI_MESSAGE.deserialize(legacyToMiniMessage(message));
    }

    public static List<Component> parse(List<?> message) {
        List<Component> components = new ArrayList<>();
        for (var i : message) {
            components.add(ColorUtils.parse(i.toString()));
        }
        return components;
    }

    public static String deParse(Component message) {
        return MINI_MESSAGE.serialize(message);
    }

    public static List<String> deParse(List<Component> message) {
        List<String> list = new ArrayList<>();
        for (Component i : message) {
            list.add(deParse(i));
        }
        return list;
    }
}
