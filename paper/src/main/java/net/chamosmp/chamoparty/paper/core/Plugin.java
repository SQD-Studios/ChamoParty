package net.chamosmp.chamoparty.paper.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.chamosmp.chamoparty.api.enums.InventoryName;
import net.chamosmp.chamoparty.api.storage.Script;
import net.chamosmp.chamoparty.core.enums.Folder;
import net.chamosmp.chamoparty.core.utils.plugins.Plugins;
import net.chamosmp.chamoparty.exceptions.ListenerNullException;
import net.chamosmp.chamoparty.paper.adapter.PlayerAdapter;
import net.chamosmp.chamoparty.paper.adapter.RewardAdapter;
import net.chamosmp.chamoparty.paper.adapter.VoteAdapter;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.api.Vote;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.utils.storage.Persist;
import net.chamosmp.chamoparty.paper.core.utils.storage.Saveable;
import net.chamosmp.chamoparty.paper.listener.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class Plugin extends JavaPlugin {

    private final List<Saveable> savers = new ArrayList<>();
    private final List<ListenerAdapter> listenerAdapters = new ArrayList<>();
    private final List<String> files = new ArrayList<>();
    private Gson gson;
    private Persist persist;
    private long enableTime;

    protected void preEnable() {

        this.enableTime = System.currentTimeMillis();

        Logger.log("Enabling");
        Logger.log("Plugin Version v" + this.getPluginMeta().getVersion(), LogType.INFO);

        this.getDataFolder().mkdirs();

        for (Folder folder : Folder.values()) {
            File currentFolder = new File(this.getDataFolder(), folder.toFolder());
            if (!currentFolder.exists()) currentFolder.mkdir();
        }

        this.gson = getGsonBuilder().create();
        this.persist = new Persist(this);

        for (String file : this.files) {
            if (!new File(getDataFolder() + "/inventories/" + file + ".yml").exists()) {
                saveResource("inventories/" + file + ".yml", false);
            }
        }

        for (Script script : Script.values()) {
            if (!new File(getDataFolder() + "/scripts/" + script.name().toLowerCase() + ".sql").exists()) {
                this.saveResource("scripts/" + script.name().toLowerCase() + ".sql", false);
            }
        }

        if (!Bukkit.getServerConfig().isProxyOnlineMode()) {
            Logger.log("""
                    It appears that you are running an offline mode server. We, do not provide support for setups that bypass Mojang's authentication.
                    You are on your own to solve any issues that arise.
                    """, LogType.WARNING);
        }
    }

    protected void postEnable() {
        Logger.log("Done enabling (" + Math.abs(enableTime - System.currentTimeMillis()) + "ms)");
    }

    protected void preDisable() {
        this.enableTime = System.currentTimeMillis();
        Logger.log("Starting disabling");
    }

    protected void postDisable() {
        Logger.log("Done disabling (" + Math.abs(enableTime - System.currentTimeMillis()) + "ms)");
    }

    /**
     * Build gson
     *
     * @return
     */
    public GsonBuilder getGsonBuilder() {
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).registerTypeAdapter(PlayerVote.class, new PlayerAdapter(this)).registerTypeAdapter(Vote.class, new VoteAdapter(this)).registerTypeAdapter(Reward.class, new RewardAdapter(this));
    }

    /**
     * Add a listener
     *
     * @param listener
     */
    public void addListener(Listener listener) {
        if (listener instanceof Saveable) this.addSave((Saveable) listener);
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    /**
     * Add a listener from ListenerAdapter
     *
     * @param adapter
     */
    public void addListener(ListenerAdapter adapter) {
        if (adapter == null) throw new ListenerNullException("Warning, your listener is null");
        if (adapter instanceof Saveable) this.addSave((Saveable) adapter);
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
     * Get gson
     *
     * @return {@link Gson}
     */
    public Gson getGson() {
        return gson;
    }

    public Persist getPersist() {
        return persist;
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
     * @param classz
     * @return
     */
    public <T> T getProvider(Class<T> classz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
        if (provider == null) {
            Logger.log("Unable to retrieve the provider " + classz, LogType.WARNING);
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
    protected boolean isEnable(Plugins pl) {
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

    protected void registerFile(InventoryName file) {
        this.files.add(file.getName());
    }
}
