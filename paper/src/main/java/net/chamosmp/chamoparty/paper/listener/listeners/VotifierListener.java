package net.chamosmp.chamoparty.paper.listener.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
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
        if (LegacyJsonConfig.storage == Storage.VELOCITY && "backend".equals(plugin.getConfig().getString("database.velocity.where-is-the-votifier", "backend").toLowerCase()))
            return;
        VotePartyManager manager = this.plugin.getManager();

        Vote vote = event.getVote();
        manager.vote(vote.getUsername(), vote.getServiceName(), true);
    }
}