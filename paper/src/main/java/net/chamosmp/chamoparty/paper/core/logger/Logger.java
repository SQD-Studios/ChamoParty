package net.chamosmp.chamoparty.paper.core.logger;

import net.chamosmp.chamoparty.core.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.slf4j.LoggerFactory;

public class Logger {

    private static final Component prefix = ColorUtils.parse("<aqua>ChamoParty| </aqua>");
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);

    public Logger() {
    }

    public enum LogType {
        ERROR("<dark_red>"),
        INFO("<yellow>"),
        WARNING("<red>"),
        SUCCESS("<green>");

        private final String color;

        LogType(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }

    public static void log(String message, LogType type) {
        Bukkit.getConsoleSender().sendMessage(prefix.append(ColorUtils.parse(type.getColor() + message)));
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix.append(ColorUtils.parse(message)));
    }
}
