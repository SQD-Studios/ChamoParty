package net.chamosmp.chamoparty.zcore.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class ColorUtils {
    /**
     * What are these weird things I do for YOU
     * @apiNote It's not finished yet so write it in MiniMessage to begin with
     * @param message The message to make the Legacy to minimessage
     * @return The minimessage from legacy String
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
        String minecoin_gold = white.replace("&g", "<#DDD605>");
        String material_quartz = minecoin_gold.replace("&h", "<#E3D4D1>");
        String material_iron = material_quartz.replace("&i", "<#CECACA>");
        String material_netherite = material_iron.replace("&j", "<#443A3B>");
        //String material_redstone = material_netherite.replace("&m", "<#971607>");
        //String material_copper = material_redstone.replace("&n", "<#B4684D>");
        String material_gold = material_netherite.replace("&p", "<#DEB12D>");
        String material_emerald = material_gold.replace("&q", "<#119F36>");
        String material_diamond = material_emerald.replace("&s", "<#2CBAA8>");
        String material_lapis = material_diamond.replace("&t", "<#21497B>");
        String material_amethyst = material_lapis.replace("&u", "<#9A5CC6>");
        String material_resin = material_amethyst.replace("&v", "<#EB7114>");
        String party_blue_color = material_resin.replace("&w", "<#8CB3FF>");
        String bold = party_blue_color.replace("&l", "<b>");
        String italic = bold.replace("&o", "<i>");
        String underline = italic.replace("&n", "<u>");
        String strikethrough = underline.replace("&m", "<st>");
        String obfuscated = strikethrough.replace("&k", "<obf>");

        return obfuscated;
    }
}
