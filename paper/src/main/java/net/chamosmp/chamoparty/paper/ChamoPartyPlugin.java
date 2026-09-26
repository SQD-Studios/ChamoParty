package net.chamosmp.chamoparty.paper;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.chamosmp.chamoparty.core.utils.plugins.Plugins;
import net.chamosmp.chamoparty.paper.api.PlayerManager;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.commands.BaseCommandBrigadier;
import net.chamosmp.chamoparty.paper.commands.VoteCommandBrigadier;
import net.chamosmp.chamoparty.paper.listener.VotifierListener;
import net.chamosmp.chamoparty.paper.loader.ZMenuLoader;
import net.chamosmp.chamoparty.paper.placeholder.PlaceholderAPI;
import net.chamosmp.chamoparty.paper.placeholder.VotePartyExpansion;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import net.chamosmp.chamoparty.paper.save.MessageLoader;
import net.chamosmp.chamoparty.paper.database.DatabaseManager;
import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import net.chamosmp.sqdlib.paper.util.SchedulerUtil;
import net.chamosmp.sqdlib.paper.util.UpdateUtil;
import net.chamosmp.sqdlib.util.LogType;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

public class ChamoPartyPlugin extends JavaPlugin {

    private final VotePartyManager manager = new ChamoPartyManager(this);
    private ZMenuLoader loader;
    private net.chamosmp.chamoparty.paper.api.storage.StorageManager storageManager;
    private MessageLoader messageLoader;

    @Override
    public void onEnable() {
        new LoggerUtil("<aqua>chamoParty</aqua>| ");
        PlaceholderAPI.getInstance().setPlugin(this);

        this.getDataFolder().mkdirs();

        File inventoryFile = new File(getDataFolder(), "/inventories/vote.yml");
        if (!inventoryFile.exists()) {
            saveResource("inventories/vote.yml", false);
        }

        if (!Bukkit.getServerConfig().isProxyOnlineMode()) {
            LoggerUtil.log(LogType.WARNING, """
                    It appears that you are running an offline mode server. We, do not provide support for setups that bypass Mojang's authentication.
                    You are on your own to solve any issues that arise.""");
        }

        this.saveDefaultConfig();
        this.reloadConfig();


        this.getServer().getServicesManager().register(VotePartyManager.class, this.manager, this, ServicePriority.High);

        /*
        Commands
        */
        registerCommands();

        this.messageLoader = new MessageLoader(this);
        this.messageLoader.load();

        // Load storage
        LegacyJsonConfig.loadConfigOptions(this);
        this.storageManager = new DatabaseManager(LegacyJsonConfig.storage, this);
        this.storageManager.load();

        this.manager.loadConfiguration();

        if (this.isEnabled(Plugins.PLACEHOLDER)) {
            VotePartyExpansion expansion = new VotePartyExpansion(this);
            expansion.register();
        }

        if (this.isEnabled(Plugins.VOTIFIER)) {
            LoggerUtil.log(LogType.INFO, "Hooked into (Nu)Votifier");
            Bukkit.getPluginManager().registerEvents(new VotifierListener(this), this);
        }


        if (this.isEnabled(Plugins.ZMENU)) {
            SchedulerUtil.runDelayed(this, () -> {
                this.loader = new ZMenuLoader(this);
                this.loader.load();
                if (this.loader.isLoaded()) {
                    reloadInventories();
                } else {
                    LoggerUtil.log(LogType.WARNING, "Failed to hook into zMenu.");
                }
            }, 1L);
        }

        try {
            UpdateUtil checker = new UpdateUtil(this, "chamoparty", "https://modrinth.com/plugin/chamoparty");
            checker.versionCheck();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        /*
        Metrics
         */
        try {
            new Metrics(this, 31621);
            LoggerUtil.log(LogType.INFO, "Successfully started metrics!");
        } catch (Exception ignored) {
            LoggerUtil.log(LogType.SEVERE, "Failed to hook into Metrics.");
        }

        LoggerUtil.log(LogType.INFO, "Done enabling");
    }

    @Override
    public void onDisable() {
        this.messageLoader.save();
        this.storageManager.save();

        LoggerUtil.log(LogType.INFO, "Done disabling");
    }

    public void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            BaseCommandBrigadier.register(event.registrar(), this);
            VoteCommandBrigadier.register(event.registrar(), this);
        }));
    }


    /**
     * Return the manager for the voteparty
     *
     * @return {@link VotePartyManager}
     */
    public VotePartyManager getManager() {
        return manager;
    }

    public PlayerManager getPlayerManager() {
        return this.storageManager.getIStorage();
    }

    public IStorage getIStorage() {
        return this.storageManager.getIStorage();
    }

    /**
     * Get player vote
     *
     * @param offlinePlayer
     * @return {@link PlayerVote}
     */
    public void get(OfflinePlayer offlinePlayer, Consumer<PlayerVote> consumer) {
        this.get(offlinePlayer.getUniqueId(), consumer);
    }

    /**
     * Get player vote
     *
     * @param uuid
     * @return {@link PlayerVote}
     */
    public void get(UUID uuid, Consumer<PlayerVote> consumer) {
        PlayerManager manager = this.getPlayerManager();
        manager.getPlayer(uuid, optional -> {
            consumer.accept(optional.orElseGet(() -> manager.createPlayer(uuid)));
        }, true);
    }

    /*
    Inventory/zMenu
     */
    public void reloadInventories() {
        if (this.loader == null) return; // zMenu not present
        this.loader.reload();
    }

    public ZMenuLoader getLoader() {
        return loader;
    }

    private boolean isEnabled(Plugins pl) {
        return Bukkit.getPluginManager().isPluginEnabled(pl.getName());
    }

    public MessageLoader getMessageLoader() {
        return messageLoader;
    }
}
