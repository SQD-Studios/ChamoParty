package net.chamosmp.chamoparty.paper.implementations;

import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import net.chamosmp.sqdlib.paper.util.SchedulerUtil;
import net.chamosmp.sqdlib.util.LogType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class ChamoReward extends Utils implements Reward {

    private final double percent;
    private final List<String> commands;
    private final boolean needToBeOnline;
    private final List<String> messages;

    public ChamoReward(double percent, List<String> commands, boolean needToBeOnline, List<String> messages) {
        super();
        this.percent = percent;
        this.commands = commands;
        this.needToBeOnline = needToBeOnline;
        this.messages = messages;
    }

    @Override
    public double getPercent() {
        return this.percent;
    }

    @Override
    public List<String> getCommands() {
        return this.commands;
    }

    @Override
    public boolean needToBeOnline() {
        return this.needToBeOnline;
    }

    @Override
    public List<String> getMessages() {
        return this.messages;
    }

    @Override
    public void give(Plugin plugin, OfflinePlayer player) {
        if (player == null) {
            LoggerUtil.log(LogType.WARNING, "Player is null. Cannot give reward.");
            return;
        }

        // Pre-filter commands and messages
        List<String> validCommands = (this.commands == null) ? List.of() :
                this.commands.stream().filter(cmd -> cmd != null && !cmd.trim().isEmpty()).toList();

        List<String> validMessages = (this.messages == null) ? List.of() :
                this.messages.stream().filter(msg -> msg != null && !msg.trim().isEmpty()).toList();

        // Handle commands and percent warnings
        boolean percentInvalid = this.percent <= 0;

        if (validCommands.isEmpty() && percentInvalid) {
        } else if (validCommands.isEmpty()) {
        } else if (percentInvalid) {
        } else {
            SchedulerUtil.runDelayed(plugin, () ->
                    validCommands.forEach(command ->
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()))
                    ), 1L
            );
        }

        // Handle messages
        if (!validMessages.isEmpty()) {
            Bukkit.getOnlinePlayers().forEach(oPlayer ->
                    validMessages.forEach(message ->
                            this.messageWO(oPlayer, papi(message, oPlayer), Map.of("player", player.getName()))
                    )
            );
        }
    }
}
