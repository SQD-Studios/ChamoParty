package net.chamosmp.chamoparty.paper.core.logger;

import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public final class Logger {

    private Logger() {
    }

    public enum LogType {
        ERROR,
        INFO,
        WARNING,
        SUCCESS;

        public net.chamosmp.sqdlib.util.LogType logType() {
            return switch (this) {
                case ERROR -> net.chamosmp.sqdlib.util.LogType.SEVERE;
                case INFO, SUCCESS -> net.chamosmp.sqdlib.util.LogType.INFO;
                case WARNING -> net.chamosmp.sqdlib.util.LogType.WARNING;
            };
        }
    }

    public static void log(String message, LogType type) {
        LoggerUtil.log(type.logType(), message);
    }

    public static void log(String message) {
        LoggerUtil.log(net.chamosmp.sqdlib.util.LogType.INFO, message);
    }
}