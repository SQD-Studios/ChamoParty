package net.chamosmp.chamoparty.paper.core;

import net.chamosmp.chamoparty.core.utils.plugins.Plugins;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.utils.storage.Saveable;
import net.chamosmp.chamoparty.paper.listener.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Plugin extends JavaPlugin {

    private final List<Saveable> savers = new ArrayList<>();
    private final List<ListenerAdapter> listenerAdapters = new ArrayList<>();

    protected void preEnable() {
        this.getDataFolder().mkdirs();

        File inventoryFile = new File(getDataFolder(), "/inventories/vote.yml");
        if (!inventoryFile.exists()) {
            saveResource("inventories/vote.yml", false);
        }

        if (!Bukkit.getServerConfig().isProxyOnlineMode()) {
            Logger.log("""
                    It appears that you are running an offline mode server. We, do not provide support for setups that bypass Mojang's authentication.
                    You are on your own to solve any issues that arise.""", LogType.WARNING);
        }
    }

    protected void postEnable() {
        Logger.log("Done enabling");
    }

    protected void postDisable() {
        Logger.log("Done disabling");
    }


    /**
     * Add a listener
     *
     * @param listener
     */
    public void addListener(Listener listener) {
        if (listener instanceof Saveable saveable) this.addSave(saveable);
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    /**
     * Add a listener from ListenerAdapter
     *
     * @param adapter
     */
    public void addListener(@NotNull ListenerAdapter adapter) {
        if (adapter instanceof Saveable saveable) this.addSave(saveable);
        this.listenerAdapters.add(adapter);
    }

    /**
     * Add a Saveable
     *
     * @param saver
     */
    public void addSave(Saveable saver) {
        this.savers.add(saver);
    }

    /**
     * Get all saveables
     *
     * @return savers
     */
    public List<Saveable> getSavers() {
        return savers;
    }

    /**
     * @param providerClass
     * @return
     */
    public <T> T getProvider(Class<T> providerClass) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(providerClass);
        if (provider == null) {
            Logger.log("Unable to retrieve the provider " + providerClass, LogType.WARNING);
            return null;
        }
        return provider.getProvider();
    }

    /**
     * @return listenerAdapters
     */
    public List<ListenerAdapter> getListenerAdapters() {
        return listenerAdapters;
    }

    /**
     * Check if plugin is enable
     *
     * @param pluginName
     * @return
     */
    protected boolean isEnabled(Plugins pl) {
        org.bukkit.plugin.Plugin plugin = getPlugin(pl);
        return plugin != null && plugin.isEnabled();
    }

    /**
     * Get plugin for plugins enum
     *
     * @param plugin
     * @return
     */
    protected org.bukkit.plugin.Plugin getPlugin(Plugins plugin) {
        return Bukkit.getPluginManager().getPlugin(plugin.getName());
    }
}
