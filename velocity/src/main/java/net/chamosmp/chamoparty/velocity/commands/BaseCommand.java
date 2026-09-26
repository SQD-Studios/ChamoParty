package net.chamosmp.chamoparty.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.chamosmp.chamoparty.velocity.ChamoPartyVelo;
import net.chamosmp.chamoparty.velocity.config.YamlLoader;
import net.strokkur.commands.Aliases;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.permission.Permission;
import org.spongepowered.configurate.ConfigurateException;

@Command("chamopartyv")
@Aliases({"chamopartyvelocity", "votepartyv", "votepartyvelocity"})
public class BaseCommand {
    private final YamlLoader loader;
    private final ChamoPartyVelo plugin;

    public BaseCommand(YamlLoader loader, ChamoPartyVelo plugin) {
        this.loader = loader;
        this.plugin = plugin;
    }

    @Executes("reload")
    @Permission("chamoparty.reload")
    public void onReload(CommandSource sender) {
        try {
            if (sender instanceof ConsoleCommandSource) {
                sender.sendRichMessage("Reloading config...");
                loader.reloadConfig();
                sender.sendRichMessage("Config reloaded!");
            } else {
                sender.sendRichMessage("Reloading config...");
                plugin.getLoggerUtil().info("Reloading config...");
                loader.reloadConfig();
                sender.sendRichMessage("Config reloaded!");
                plugin.getLoggerUtil().info("Config reloaded!");
            }
        } catch (ConfigurateException e) {
            sender.sendRichMessage("<dark_red>Failed to load config! Please check your proxy's console for details.");
            plugin.getLoggerUtil().error("Failed to reload config!", e);
        }
    }
}