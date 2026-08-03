package net.chamosmp.chamoparty.paper.command;


import net.chamosmp.chamoparty.core.enums.EnumInventory;
import net.chamosmp.chamoparty.paper.ChamoPartyManager;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.strokkur.commands.Aliases;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.paper.Executor;
import net.strokkur.commands.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.lang.String.valueOf;

@Command("chamoparty")
@Aliases("voteparty")
public class Base extends ChamoPartyManager {

    private final ChamoPartyPlugin plugin;

    public Base(ChamoPartyPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    // Main Command
    @Permission("chamoparty.use")
    @Executes
    void onExecuteBase(CommandSender sender) {
        sendNeedVote(sender);
    }

    // Reload Subcommand
    @Permission("chamoparty.reload")
    @Executes("reload")
    void onExecuteReload(CommandSender sender) {
        reload(sender);
    }

    // Version Subcommand
    @Permission("chamoparty.version")
    @Executes("version")
    void onExecuteVersion(CommandSender sender) {
        message(sender, String.format("""
                <green>Version<gray>: <dark_green>" %s
                <green>Organization<gray>: <dark_green>SQD Studios
                <green>Download<gray>: <dark_green>https://modrinth.com/project/chamoparty"
                """, plugin.getPluginMeta().getVersion()
        ));
    }

    // StartParty Sub
    @Permission("chamoparty.startparty")
    @Executes("startparty")
    void onExecuteStartParty(CommandSender sender) {
        forceStart(sender);
    }

    @Permission("chamoparty.help")
    @Executes("help")
    void onExecuteHelp(CommandSender sender) {
    }

    @Permission("chamoparty.config")
    @Executes("config")
    void onExecuteConfig(@Executor Player sender) {
        try {
            this.createInventory(plugin, sender, EnumInventory.INVENTORY_CONFIG);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Permission("chamoparty.add")
    @Executes("add")
    void onExecuteAdd(CommandSender sender, Player target) {
        String player = valueOf(target);
        vote(sender, player, true);
    }

    @Permission("chamoparty.remove")
    @Executes("remove")
    void onExecuteRemove(CommandSender sender, Player playerName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(String.valueOf(playerName));
        removeVote(sender, target);
    }

}
