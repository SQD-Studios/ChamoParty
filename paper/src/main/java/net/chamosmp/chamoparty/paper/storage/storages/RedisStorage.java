package net.chamosmp.chamoparty.paper.storage.storages;

import java.util.UUID;

import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.storage.redis.RedisClient;
import net.chamosmp.chamoparty.paper.storage.redis.ServerMessaging;
import net.chamosmp.chamoparty.paper.core.utils.ElapsedTime;
import net.chamosmp.chamoparty.paper.core.utils.storage.Persist;


public class RedisStorage extends SqlStorage implements IStorage {

	private final RedisClient redisClient;
	private final ServerMessaging messaging;

	/**
	 * @param storage
	 * @param plugin
	 */
	public RedisStorage(Storage storage, ChamoPartyPlugin plugin) {
		super(plugin, storage);
		this.redisClient = new RedisClient();
		this.messaging = new ServerMessaging(plugin, this, this.redisClient);
	}

	@Override
	public void load(Persist persist) {
		super.load(persist);
	}

	@Override
	public void save(Persist persist) {
		super.save(persist);
		try {
			this.messaging.stop();
		} catch (Exception e) {
		}
	}

	@Override
	public void performCustomVoteAction(String username, String serviceName, UUID uuid) {
		this.messaging.sendVoteAction(username, serviceName, uuid);
	}

	/**
	 * Add vote count but its a secret
	 * 
	 * @param i
	 */
	public void addSecretVoteCount(int i) {
		this.voteCount += i;
	}

	@Override
	public void addVoteCount(long amount) {
		super.addVoteCount(amount);
		ElapsedTime elapsedTime = new ElapsedTime("Redis message");
		elapsedTime.start();
		this.messaging.sendAddVoteCount();
		elapsedTime.endDisplay();
	}

	@Override
	public void startVoteParty() {
		super.startVoteParty();
		this.messaging.sendHandleVoteParty();
	}

	public void setSecretVoteCount(int i) {
		this.voteCount = 0;
	}

}
