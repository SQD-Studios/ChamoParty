package net.chamosmp.chamoparty.paper.commands;


import net.chamosmp.chamoparty.paper.ChamoPartyManager;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.strokkur.commands.Aliases;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("chamoparty")
@Aliases("voteparty")
public class BaseCommand extends ChamoPartyManager {

    public BaseCommand(ChamoPartyPlugin plugin) {
        super(plugin);
    }

    @Permission("chamoparty.use")
    @Executes
    public void onExecuteBase(CommandSender sender) {
        sendNeedVote(sender);
    }

    @Permission("chamoparty.reload")
    @Executes("reload")
    public void onExecuteReload(CommandSender sender) {
        reload(sender);
    }

    @Permission("chamoparty.startparty")
    @Executes("startparty")
    public void onExecuteStartParty(CommandSender sender) {
        forceStart(sender);
    }

    @Permission("chamoparty.add")
    @Executes("add")
    public void onExecuteAdd(CommandSender sender, Player target) {
        vote(sender, target.getName(), true);
    }

    @Permission("chamoparty.remove")
    @Executes("remove")
    public void onExecuteRemove(CommandSender sender, Player playerName) {
        removeVote(sender, Bukkit.getOfflinePlayer(String.valueOf(playerName)));
    }
}