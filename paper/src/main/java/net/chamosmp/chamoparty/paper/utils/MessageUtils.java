package net.chamosmp.chamoparty.paper.utils;

import net.chamosmp.chamoparty.api.enums.Message;
import net.chamosmp.sqdlib.paper.util.ColorUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class MessageUtils extends PapiUtils {

    public static void message(Audience audience, String message, Map<?, ?> args) {
        audience.sendMessage(getMessage(message, args));
    }

    public static void message(Audience audience, String message) {
        audience.sendMessage(getMessage(message, Map.of()));
    }

    public static void actionMessage(Audience player, Message message, Map<?, ?> args) {
        player.sendActionBar(getMessage(message, args));
    }

    public static Component getMessage(Message message, Map<?, ?> args) {
        return getMessage(message.getMessage(), args);
    }

    public static Component getMessage(String message, Map<?, ?> args) {
        return ColorUtil.parse(null, message, args);
    }

    public static Component getMessage(Component message, Map<?, ?> args) {
        String deParsed = ColorUtil.deParse(message);
        return ColorUtil.parse(null, deParsed, args);
    }

    public static void title(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        player.showTitle(Title.title(ColorUtil.parse(title), ColorUtil.parse(subtitle), fadeInTime, showTime, fadeOutTime));
    }

    public void message(Audience sender, Message message, Map<?, ?> args) {
        if (sender instanceof Player player) {
            switch (message.getType()) {
                case ACTION:
                    actionMessage(player, message, args);
                    break;
                case TCHAT:
                    sender.sendMessage(Message.PREFIX.getMessage().append(papi(getMessage(message, args), player)));
                    break;
                case TITLE:
                    String title = message.getTitle();
                    String subTitle = message.getSubTitle();
                    int fadeInTime = message.getStart();
                    int showTime = message.getTime();
                    int fadeOutTime = message.getEnd();
                    title(player, this.papi(title, player), this.papi(subTitle, player), fadeInTime, showTime,
                            fadeOutTime);
                    break;
            }
        } else {
            Component messageComponent = Message.PREFIX.getMessage().append(papi(getMessage(message, args), null));
            sender.sendMessage(messageComponent);
        }
    }

    public void message(Audience sender, Message message) {
        message(sender, message, Map.of());
    }

    public void broadcast(Message message, Map<?, ?> args) {
        message(Bukkit.getServer(), message, args);
    }
}
