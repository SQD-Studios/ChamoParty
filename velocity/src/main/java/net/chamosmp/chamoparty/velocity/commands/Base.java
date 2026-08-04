package net.chamosmp.chamoparty.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import net.chamosmp.chamoparty.velocity.ChamoPartyVelo;
import net.chamosmp.chamoparty.velocity.config.YamlLoader;
import net.strokkur.commands.Aliases;
import net.strokkur.commands.Command;
import net.strokkur.commands.Executes;
import net.strokkur.commands.permission.Permission;
import org.spongepowered.configurate.ConfigurateException;

@Command("chamopartyv")
@Aliases({"chamopartyvelocity", "votepartyv", "votepartyvelocity"})
public class Base {
    private final YamlLoader loader;
    private final ChamoPartyVelo plugin;

    public Base(YamlLoader loader, ChamoPartyVelo plugin) {
        this.loader = loader;
        this.plugin = plugin;
    }

    @Executes
    @Permission("chamoparty.use")
    public void onBase(Player sender) {

    }

    @Executes("reload")
    @Permission("chamoparty.reload")
    public void onReload(Player sender) {
        try {
            loader.reloadConfig();
            plugin.getLogger().info("Reloading config...");
        } catch (ConfigurateException e) {
            plugin.getLogger().error("Failed to reload config!", e);
        }
    }
}
