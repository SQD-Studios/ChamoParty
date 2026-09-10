package net.chamosmp.chamoparty.paper.listener.listeners;

import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import net.chamosmp.chamoparty.paper.listener.ListenerAdapter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class GiveRemainingVotesListener extends ListenerAdapter {

    private final ChamoPartyPlugin plugin;

    /**
     * @param plugin
     */
    public GiveRemainingVotesListener(ChamoPartyPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    protected void onConnect(PlayerJoinEvent event, Player player) {
        VotePartyManager manager = this.plugin.getManager();
        manager.giveVotes(player);
    }
}