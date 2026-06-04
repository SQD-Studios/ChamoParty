package net.chamosmp.chamoparty.command.brigadier;

import net.chamosmp.chamoparty.ZVotePartyPlugin;
import net.chamosmp.chamoparty.api.enums.Message;
import net.chamosmp.chamoparty.command.VCommand;
import net.chamosmp.chamoparty.zcore.enums.EnumInventory;
import net.chamosmp.chamoparty.zcore.utils.commands.CommandType;
import net.strokkur.commands.Aliases;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.lang.String.valueOf;

@Command("chamoparty")
@Aliases("voteparty")
public class Base extends VCommand {
    public Base(ZVotePartyPlugin plugin) {
        super(plugin);
    }

    // Main Command
    @Permission("chamoparty.use")
    @Executes
    void onExecuteBase(CommandSender sender) {
        this.manager.sendNeedVote(sender);
    }

    // Reload Subcommand
    @Permission("chamoparty.reload")
    @Executes("reload")
    void onExecuteReload(CommandSender sender) {
        this.manager.reload(sender);
    }

    // Version Subcommand
    @Permission("chamoparty.version")
    @Executes("version")
    void onExecuteVersion(CommandSender sender) {
        message(sender, "<green>Version<gray>: <dark_green>" + plugin.getPluginMeta().getVersion());
        message(sender, "<green>Organization<gray>: <dark_green>SQD Studios");
        message(sender, "<green>Download<gray>: <dark_green>https://modrinth.com/project/chamoparty");
    }

    // StartParty Sub
    @Permission("chamoparty.startparty")
    @Executes("startparty")
    void onExecuteStartParty(CommandSender sender) {
        this.manager.forceStart(sender);
    }

    @Permission("chamoparty.help")
    @Executes("help")
    void onExecuteHelp(CommandSender sender) {
        this.parent.getSubVCommands().forEach(command -> {
            if (command.getPermission() == null || sender.hasPermission(command.getPermission())) {
                messageWO(sender, Message.COMMAND_SYNTAX_HELP, "%syntax%", command.getSyntax(), "%description%",
                        command.getDescription());
            }
        });
    }

    @Permission("chamoparty.config")
    @Executes("config")
    void onExecuteConfig(Player sender) {
        this.createInventory(plugin, sender, EnumInventory.INVENTORY_CONFIG);
    }

    @Permission("chamoparty.add")
    @Executes("add")
    void onExecuteAdd(CommandSender sender, Player target) {
        String player = valueOf(target);
        boolean updateVoteParty = this.argAsBoolean(1, false);
        this.plugin.getManager().vote(sender, player, updateVoteParty);
    }

    @Permission("chamoparty.remove")
    @Executes("remove")
    void onExecuteRemove(CommandSender sender, Player playerName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(String.valueOf(playerName));
        this.plugin.getManager().removeVote(sender, target);
    }



    @Override
    protected CommandType perform(ZVotePartyPlugin plugin) {
        return null;
    }
}
