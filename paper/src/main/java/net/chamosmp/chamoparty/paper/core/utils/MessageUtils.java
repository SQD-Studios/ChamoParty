package net.chamosmp.chamoparty.paper.core.utils;

import net.chamosmp.chamoparty.api.enums.Message;
import net.chamosmp.chamoparty.core.enums.DefaultFontInfo;
import net.chamosmp.chamoparty.core.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class MessageUtils extends PapiUtils {

    /**
     *
     * @param player  The player to send the message to
     * @param message The message to send
     * @param args    The arguments?
     */
    protected void messageWO(CommandSender player, Message message, Object... args) {
        player.sendMessage(getMessage(message, args));
    }

    protected void messageWO(CommandSender player, String message, Object... args) {
        player.sendMessage(getMessage(message, args));
    }

    protected void message(CommandSender player, String message, Object... args) {
        player.sendMessage(getMessage(message, args));
    }

    /**
     *
     * @param sender  The player to send the message to
     * @param message The message to send
     * @param args    The arguments?
     */
    protected void message(CommandSender sender, Message message, Object... args) {
        if (sender instanceof ConsoleCommandSender) {
            if (!message.getMessages().isEmpty()) {
                message.getMessages().forEach(msg -> {
                    Component messageComponent = Message.PREFIX.getMessage().append(this.papi(getMessage(msg, args), null));
                    sender.sendMessage(messageComponent);
                });
            } else {
                Component messageComponent = Message.PREFIX.getMessage().append(this.papi(getMessage(message, args), null));
                sender.sendMessage(messageComponent);
            }
        } else {

            Player player = (Player) sender;
            switch (message.getType()) {
                case ACTION:
                    actionMessage(player, message, args);
                    break;
                case TCHAT:
                    if (!message.getMessages().isEmpty()) {
                        message.getMessages().forEach(msg -> {
                            sender.sendMessage(Message.PREFIX.getMessage().append(papi(getMessage(msg, args), player)));
                        });
                    } else
                        sender.sendMessage(Message.PREFIX.msg().append(papi(getMessage(message, args), player)));
                    break;
                case CENTER:
                    if (!message.getMessages().isEmpty()) {
                        message.getMessages().forEach(msg -> {
                            sender.sendMessage(papi(getCenteredMessage(getMessage(msg, args)), player));
                        });
                    } else
                        sender.sendMessage(papi(getCenteredMessage(getMessage(message, args)), player));
                    break;
                case TITLE:
                    // gestion du title message
                    String title = message.getTitle();
                    String subTitle = message.getSubTitle();
                    int fadeInTime = message.getStart();
                    int showTime = message.getTime();
                    int fadeOutTime = message.getEnd();
                    this.title(player, this.papi(title, player), this.papi(subTitle, player), fadeInTime, showTime,
                            fadeOutTime);
                    break;
                default:
                    break;

            }

        }

    }

    /**
     *
     * @param player
     * @param message
     * @param args
     */
    protected void broadcast(Message message, Object... args) {
        for (Player player : Bukkit.getOnlinePlayers())
            message(player, message, args);
        message(Bukkit.getConsoleSender(), ColorUtils.legacyToMiniMessage(String.valueOf(message)), args);
    }

    /**
     *
     * @param player
     * @param message
     * @param args
     */
    protected void actionMessage(Player player, Message message, Object... args) {
        player.sendActionBar(getMessage(message, args));
    }

    protected Component getMessage(Message message, Object... args) {
        return getMessage(message.getMessage(), args);
    }

    protected Component getMessage(String message, Object... args) {
        if (args.length % 2 != 0)
            System.err.println("This/The method cannot be applied to messages.");
        else
            for (int a = 0; a < args.length; a += 2) {
                String replace = args[a].toString();
                String to = args[a + 1].toString();
                message = message.replace(replace, to);
            }
        return ColorUtils.parse(message);
    }

    protected Component getMessage(Component message, Object... args) {
        if (args.length % 2 != 0) {
            System.err.println("This/The method cannot be applied to messages.");
            return message;
        }
        String deParsed = ColorUtils.deParse(message);
        for (int a = 0; a < args.length; a += 2) {
            String replace = args[a].toString();
            String to = args[a + 1].toString();
            deParsed = deParsed.replace(replace, to);
        }
        return ColorUtils.parse(deParsed);
    }

    /**
     * Send title to player
     *
     * @param player
     * @param title
     * @param subtitle
     * @param fadeInTime
     * @param showTime
     * @param fadeOutTime
     */
    protected void title(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        player.showTitle(Title.title(ColorUtils.parse(title), ColorUtils.parse(subtitle), fadeInTime, showTime, fadeOutTime));
    }

    private final static int CENTER_PX = 154;

    /**
     *
     * @param message The message you want to send
     * @return message
     */
    protected Component getCenteredMessage(Component message) {
        if (message == null)
            return Component.text("");

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        String messageText = ColorUtils.deParse(message);
        for (char c : messageText.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return ColorUtils.parse(sb + messageText);
    }

}
