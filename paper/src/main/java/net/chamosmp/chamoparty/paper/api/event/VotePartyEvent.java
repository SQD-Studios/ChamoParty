package net.chamosmp.chamoparty.paper.api.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VotePartyEvent extends Event {

	private final static HandlerList handlers = new HandlerList();

	/**
	 * @return the handlers
	 */
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public boolean callEvent(){
		Bukkit.getPluginManager().callEvent(this);
		return false;
	}

}
