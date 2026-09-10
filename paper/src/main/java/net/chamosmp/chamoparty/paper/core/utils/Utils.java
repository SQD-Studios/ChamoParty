package net.chamosmp.chamoparty.paper.core.utils;

import com.google.common.base.Strings;
import net.chamosmp.chamoparty.core.utils.ProgressBar;
import net.chamosmp.sqdlib.paper.util.ColorUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Utils extends MessageUtils {

    public String getProgressBar(long l, long m, int totalBars, char symbol, String completedColor, String notCompletedColor) {
        float percent = (float) l / m;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat(completedColor + symbol, progressBars) + Strings.repeat(notCompletedColor + symbol, totalBars - progressBars);
    }

    public String getProgressBar(long l, long m, ProgressBar progressBar) {
        return this.getProgressBar(l, m, progressBar.length(), progressBar.symbol(), progressBar.completedColor(), progressBar.notCompletedColor());
    }


    /**
     * Format a double in a String
     *
     * @return formatting current duplicate
     */
    protected String format(double decimal) {
        return format(decimal, "#.##");
    }

    /**
     * Format a double in a String
     *
     * @return formatting current double according to the given format
     */
    protected String format(double decimal, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(decimal);
    }

    protected void schedule(long delay, Runnable runnable) {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                if (runnable != null) runnable.run();
            }
        }, delay);
    }

    protected String name(String string) {
        String name = string.replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    protected String name(Material string) {
        String name = string.name().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    protected Component name(ItemStack itemStack) {
        return this.getItemName(itemStack);
    }

    protected Component getItemName(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) return item.getItemMeta().customName();
        return ColorUtil.parse("");
    }

    protected Component color(String message) {
        return ColorUtil.parse(message);
    }

    protected String colorReverse(String message) {
        return message == null ? null : message.replace("§", "&");
    }

    protected String format(long l) {
        return format(l, ' ');
    }

    protected String format(long l, char c) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(c);
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(l);
    }
}
