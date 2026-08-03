package net.chamosmp.chamoparty.paper.core.utils;

import com.google.common.base.Strings;
import net.chamosmp.chamoparty.core.enums.EnumInventory;
import net.chamosmp.chamoparty.core.utils.ColorUtils;
import net.chamosmp.chamoparty.core.utils.ProgressBar;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.core.sched.SchedulerUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Utils extends MessageUtils {

    public String getProgressBar(long l, long m, int totalBars, char symbol, String completedColor, String notCompletedColor) {
        float percent = (float) l / m;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat(completedColor + symbol, progressBars) + Strings.repeat(notCompletedColor + symbol, totalBars - progressBars);
    }

    public String getProgressBar(long l, long m, ProgressBar progressBar) {
        return this.getProgressBar(l, m, progressBar.getLength(), progressBar.getSymbol(), progressBar.getCompletedColor(), progressBar.getNotCompletedColor());
    }

    /**
     * @param player
     * @return true if the player's inventory is full
     */
    protected boolean hasInventoryFull(Player player) {
        int slot = 0;
        PlayerInventory inventory = player.getInventory();
        for (int a = 0; a != 36; a++) {
            ItemStack itemStack = inventory.getContents()[a];
            if (itemStack == null) slot++;
        }
        return slot == 0;
    }

    protected boolean give(ItemStack item, Player player) {
        if (hasInventoryFull(player)) return false;
        player.getInventory().addItem(item);
        return true;
    }

    /**
     * Gives an item to the player, if the player's inventory is full then the
     * item will drop to the ground
     *
     * @param player
     * @param item
     */
    protected void give(Player player, ItemStack item) {
        if (hasInventoryFull(player)) player.getWorld().dropItem(player.getLocation(), item);
        else player.getInventory().addItem(item);
    }


    /**
     * Format a double in a String
     *
     * @param decimal
     * @return formatting current duplicate
     */
    protected String format(double decimal) {
        return format(decimal, "#.##");
    }

    /**
     * Format a double in a String
     *
     * @param decimal
     * @param format
     * @return formatting current double according to the given format
     */
    protected String format(double decimal, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(decimal);
    }

    /**
     * @param delay
     * @param runnable
     */
    protected void schedule(long delay, Runnable runnable) {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                if (runnable != null) runnable.run();
            }
        }, delay);
    }

    /**
     * @param string
     * @return
     */
    protected String name(String string) {
        String name = string.replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * @param string
     * @return
     */
    protected String name(Material string) {
        String name = string.name().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * @param string
     * @return
     */
    protected Component name(ItemStack itemStack) {
        return this.getItemName(itemStack);
    }

    /**
     * @param items
     * @return
     */
    protected int getMaxPage(Collection<?> items) {
        return (items.size() / 45) + 1;
    }

    /**
     * @param player
     * @param inventoryId
     */
    protected void createInventory(ChamoPartyPlugin plugin, Player player, EnumInventory inventory) {
        createInventory(plugin, player, inventory, 1);
    }

    /**
     * @param player
     * @param inventoryId
     * @param page
     */
    protected void createInventory(ChamoPartyPlugin plugin, Player player, EnumInventory inventory, int page) {
        createInventory(plugin, player, inventory, page, new Object() {
        });
    }

    /**
     * @param player
     * @param inventoryId
     * @param page
     * @param objects
     */
    protected void createInventory(ChamoPartyPlugin plugin, Player player, EnumInventory inventory, int page, Object... objects) {
        plugin.getZInventoryManager().createInventory(inventory, player, page, objects);
    }

    /**
     * @param player
     * @param inventory
     * @param page
     * @param objects
     */
    protected void createInventory(ChamoPartyPlugin plugin, Player player, int inventory, int page, Object... objects) {
        plugin.getZInventoryManager().createInventory(inventory, player, page, objects);
    }

    /**
     * @param item
     * @return
     */
    protected Component getItemName(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) return item.getItemMeta().customName();
        return Component.text("");
    }

    /**
     * @param message
     * @return
     */
    protected Component color(String message) {
        return ColorUtils.parse(message);
    }

    /**
     * @param message
     * @return
     */
    protected String colorReverse(String message) {
        return message == null ? null : message.replace("§", "&");
    }

    /**
     * @param messages
     * @return
     */
    protected List<Component> color(List<String> messages) {
        return messages.stream().map(this::color).collect(Collectors.toList());
    }

    /**
     * @param messages
     * @return
     */
    protected List<String> colorReverse(List<String> messages) {
        return messages.stream().map(message -> colorReverse(message)).collect(Collectors.toList());
    }


    /**
     * @param l
     * @return
     */
    protected String format(long l) {
        return format(l, ' ');
    }

    /**
     * @param l
     * @param c
     * @return
     */
    protected String format(long l, char c) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(c);
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(l);
    }

    /**
     * @param runnable
     */
    protected void runAsync(Plugin plugin, Runnable runnable) {
       SchedulerUtil.runAsync(plugin, runnable);
    }


}
