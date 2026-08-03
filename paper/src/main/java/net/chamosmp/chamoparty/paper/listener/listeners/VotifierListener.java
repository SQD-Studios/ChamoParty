package net.chamosmp.chamoparty.paper.listener.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VotifierListener implements Listener {

    private final ChamoPartyPlugin plugin;

    /**
     * @param plugin
     */
    public VotifierListener(ChamoPartyPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        VotePartyManager manager = this.plugin.getManager();

        Vote vote = event.getVote();
        manager.vote(vote.getUsername(), vote.getServiceName(), true);
    }

}
