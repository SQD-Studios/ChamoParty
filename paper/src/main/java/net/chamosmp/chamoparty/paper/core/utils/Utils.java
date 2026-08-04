package net.chamosmp.chamoparty.paper.core.utils;

import com.google.common.base.Strings;
import net.chamosmp.chamoparty.core.utils.ColorUtils;
import net.chamosmp.chamoparty.core.utils.ProgressBar;
import net.chamosmp.chamoparty.paper.core.sched.SchedulerUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
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
}
