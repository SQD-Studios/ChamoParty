package net.chamosmp.chamoparty.paper;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.chamosmp.chamoparty.api.enums.InventoryName;
import net.chamosmp.chamoparty.core.utils.plugins.Plugins;
import net.chamosmp.chamoparty.paper.api.PlayerManager;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.command.BaseBrigadier;
import net.chamosmp.chamoparty.paper.command.VoteBrigadier;
import net.chamosmp.chamoparty.paper.core.Plugin;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.sched.SchedulerUtil;
import net.chamosmp.chamoparty.paper.core.utils.plugins.VersionChecker;
import net.chamosmp.chamoparty.paper.listener.AdapterListener;
import net.chamosmp.chamoparty.paper.listener.listeners.VoteListener;
import net.chamosmp.chamoparty.paper.listener.listeners.VotifierListener;
import net.chamosmp.chamoparty.paper.loader.ZMenuLoader;
import net.chamosmp.chamoparty.paper.placeholder.PlaceholderAPI;
import net.chamosmp.chamoparty.paper.placeholder.VotePartyExpansion;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import net.chamosmp.chamoparty.paper.save.MessageLoader;
import net.chamosmp.chamoparty.paper.votestorage.StorageManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import java.util.UUID;
import java.util.function.Consumer;

public class ChamoPartyPlugin extends Plugin {

    private ZMenuLoader loader;
    private final VotePartyManager manager = new ChamoPartyManager(this);
    private net.chamosmp.chamoparty.paper.api.storage.StorageManager storageManager;

    @Override
    public void onEnable() {
        PlaceholderAPI.getInstance().setPlugin(this);

        /*
        Register inventories
         */
        for (InventoryName inventoryName : InventoryName.values())
            this.registerFile(inventoryName);

        this.preEnable();

        this.saveDefaultConfig();
        this.reloadConfig();


        this.getServer().getServicesManager().register(VotePartyManager.class, this.manager, this, ServicePriority.High);

        /*
        Commands
        */
        registerCommands();

        /*
        Add Listener
        */

        this.addListener(new AdapterListener(this));
        this.addListener(new VoteListener(this));

        /*
        Add Saver
        */
        this.addSave(new MessageLoader(this));
        this.addSave(this.manager);

        this.getSavers().forEach(saver -> saver.load(this.getPersist()));

        // Load storage
        LegacyJsonConfig.getInstance(this);
        this.storageManager = new StorageManager(LegacyJsonConfig.storage, this);
        this.storageManager.load(this.getPersist());

        this.manager.loadConfiguration();

        if (this.isEnable(Plugins.PLACEHOLDER)) {
            VotePartyExpansion expansion = new VotePartyExpansion(this);
            expansion.register();
        }

        if (this.isEnable(Plugins.VOTIFIER)) {
            Logger.log("Hooked into (Nu)Votifier");
            this.addListener(new VotifierListener(this));
        }


        if (this.isEnable(Plugins.ZMENU)) {
            SchedulerUtil.runDelayed(this, () -> {
                this.loader = new ZMenuLoader(this);
                this.loader.load();
                if (this.loader.isLoaded()) {
                    reloadInventories();
                } else {
                    this.getLogger().warning("Failed to hook into zMenu.");
                }
            }, 1L);
        }

        VersionChecker checker = new VersionChecker(this);
        try {
            checker.modrinthVersionCheck();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        /*
        Metrics
         */
        try {
            Metrics metrics = new Metrics(this, 31621);
            Logger.log("Successfully started metrics!");
        } catch (Exception ignored) {
            Logger.log("Failed to hook into Metrics.", Logger.LogType.ERROR);
        }

        this.postEnable();
    }

    @Override
    public void onDisable() {
        this.getSavers().forEach(saver -> saver.save(this.getPersist()));
        this.storageManager.save(this.getPersist());

        this.postDisable();
    }

    public void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            BaseBrigadier.register(event.registrar(), this);
            VoteBrigadier.register(event.registrar(), this);
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
    public void get(OfflinePlayer offlinePlayer, Consumer<PlayerVote> consumer, boolean forceDatabaseUpdate) {
        this.get(offlinePlayer.getUniqueId(), consumer, forceDatabaseUpdate);
    }

    /**
     * Get player vote
     *
     * @param uuid
     * @return {@link PlayerVote}
     */
    public void get(UUID uuid, Consumer<PlayerVote> consumer, boolean forceDatabaseUpdate) {
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

}
