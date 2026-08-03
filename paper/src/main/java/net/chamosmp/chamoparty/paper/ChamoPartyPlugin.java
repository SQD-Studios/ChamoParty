package net.chamosmp.chamoparty.paper;

import java.util.UUID;
import java.util.function.Consumer;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.chamosmp.chamoparty.paper.command.BaseBrigadier;
import net.chamosmp.chamoparty.paper.command.VoteBrigadier;
import net.chamosmp.chamoparty.paper.core.sched.SchedulerUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import net.chamosmp.chamoparty.paper.api.PlayerManager;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import net.chamosmp.chamoparty.api.enums.InventoryName;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.inventory.InventoryManager;
import net.chamosmp.chamoparty.paper.inventory.inventories.InventoryConfig;
import net.chamosmp.chamoparty.paper.listener.AdapterListener;
import net.chamosmp.chamoparty.paper.listener.listeners.VoteListener;
import net.chamosmp.chamoparty.paper.listener.listeners.VotifierListener;
import net.chamosmp.chamoparty.paper.loader.ZMenuLoader;
import net.chamosmp.chamoparty.paper.placeholder.VotePartyExpansion;
import net.chamosmp.chamoparty.paper.placeholder.PlaceholderAPI;
import net.chamosmp.chamoparty.paper.save.Config;
import net.chamosmp.chamoparty.paper.save.MessageLoader;
import net.chamosmp.chamoparty.paper.storage.StorageManager;
import net.chamosmp.chamoparty.paper.core.Plugin;
import net.chamosmp.chamoparty.core.enums.EnumInventory;
import net.chamosmp.chamoparty.core.utils.plugins.Plugins;
import net.chamosmp.chamoparty.paper.core.utils.plugins.VersionChecker;

public class ChamoPartyPlugin extends Plugin {

	private ZMenuLoader loader;
	private final VotePartyManager manager = new ChamoPartyManager(this);
	private net.chamosmp.chamoparty.paper.api.storage.StorageManager storageManager;

	@Override
	public void onEnable() {
		PlaceholderAPI.getInstance().setPlugin(this);

		/* Register inventories */

		for (InventoryName inventoryName : InventoryName.values())
			this.registerFile(inventoryName);

		this.preEnable();

		this.saveDefaultConfig();
		this.reloadConfig();

		this.inventoryManager = new InventoryManager(this);

		this.getServer().getServicesManager().register(VotePartyManager.class, this.manager, this,
				ServicePriority.High);

		/* Commands */

		this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
			// Register the command with the description and aliases declared as annotations:
			BaseBrigadier.register(event.registrar(), this);
			VoteBrigadier.register(event.registrar(), this);
		}));

		/* Inventories */

		this.registerInventory(EnumInventory.INVENTORY_CONFIG, new InventoryConfig());

		/* Add Listener */

		this.addListener(new AdapterListener(this));
		this.addListener(this.inventoryManager);
		this.addListener(new VoteListener(this));

		/* Add Saver */
		this.addSave(Config.getInstance());
		this.addSave(new MessageLoader(this));
		this.addSave(this.manager);

		this.getSavers().forEach(saver -> saver.load(this.getPersist()));

		// Load storage
		this.storageManager = new StorageManager(Config.storage, this);
		this.storageManager.load(this.getPersist());

		//if (Config.enableVoteCommand) {
		//	this.registerCommand("vote", new CommandVote(this));
		//}

		this.manager.loadConfiguration();

		if (Config.enableAutoUpdate) {
			// Timer timer = new Timer();
			// timer.schedule(new UpdateTimer(), Config.autoSaveSecond);
		}

		if (this.isEnable(Plugins.PLACEHOLDER)) {
			VotePartyExpansion expansion = new VotePartyExpansion(this);
			expansion.register();
		}

		if (this.isEnable(Plugins.VOTIFIER)) {
			this.getLog().log("Hook NuVotifier");
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

        int pluginId = 31621;
		Metrics metrics = new Metrics(this, pluginId);

		this.postEnable();
	}

	@Override
	public void onDisable() {
		this.preDisable();

		this.getSavers().forEach(saver -> saver.save(this.getPersist()));
		this.storageManager.save(this.getPersist());

		this.postDisable();
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
			consumer.accept(optional.isPresent() ? optional.get() : manager.createPlayer(uuid));
		}, true);
	}

	public void reloadInventories() {
		if (this.loader == null) return; // zMenu not present
		this.loader.reload();
	}

	public ZMenuLoader getLoader() {
		return loader;
	}

}
