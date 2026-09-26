package net.chamosmp.chamoparty.paper.commands;

import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import net.strokkur.commands.Aliases;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("chamoparty")
@Aliases("voteparty")
public class BaseCommand {

    private final VotePartyManager manager;

    public BaseCommand(VotePartyManager manager) {
        this.manager = manager;
    }

    @Permission("chamoparty.use")
    @Executes
    public void onExecuteBase(CommandSender sender) {
        manager.sendNeedVote(sender);
    }

    @Permission("chamoparty.reload")
    @Executes("reload")
    public void onExecuteReload(CommandSender sender) {
        manager.reload(sender);
    }

    @Permission("chamoparty.startparty")
    @Executes("startparty")
    public void onExecuteStartParty(CommandSender sender) {
        manager.forceStart(sender);
    }

    @Permission("chamoparty.add")
    @Executes("add")
    public void onExecuteAdd(CommandSender sender, Player target) {
        manager.vote(sender, target.getName(), true);
    }

    @Permission("chamoparty.remove")
    @Executes("remove")
    public void onExecuteRemove(CommandSender sender, Player playerName) {
        manager.removeVote(sender, Bukkit.getOfflinePlayer(String.valueOf(playerName)));
    }
}