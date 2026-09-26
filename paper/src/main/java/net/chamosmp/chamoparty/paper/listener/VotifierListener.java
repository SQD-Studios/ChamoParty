package net.chamosmp.chamoparty.paper.listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VotifierListener implements Listener {

    private final ChamoPartyPlugin plugin;

    public VotifierListener(ChamoPartyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        if (LegacyJsonConfig.storage == Storage.VELOCITY &&
                "backend".equalsIgnoreCase(plugin.getConfig().getString("database.velocity.where-is-the-votifier", "backend")))
            return;

        Vote vote = event.getVote();
        this.plugin.getManager().vote(vote.getUsername(), vote.getServiceName(), true);
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        this.plugin.getManager().giveVotes(event.getPlayer());
    }
}