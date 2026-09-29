package net.chamosmp.chamoparty.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import net.chamosmp.chamoparty.velocity.ChamoPartyVelo;
import net.strokkur.commands.Aliases;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.permission.Permission;

@Command("chamopartyv")
@Aliases({"chamopartyvelocity", "votepartyv", "votepartyvelocity"})
public class BaseCommand {
    private final ChamoPartyVelo plugin;

    public BaseCommand(ChamoPartyVelo plugin) {
        this.plugin = plugin;
    }

    @Executes("reload")
    @Permission("chamoparty.reload")
    public void onReload(CommandSource sender) {
        plugin.reloadConfig();
        sender.sendRichMessage("Config reloaded!");
    }
}