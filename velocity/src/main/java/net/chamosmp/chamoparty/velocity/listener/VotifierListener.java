package net.chamosmp.chamoparty.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.vexsoftware.votifier.velocity.event.VotifierEvent;
import net.chamosmp.chamoparty.velocity.ChamoPartyVelo;
import net.chamosmp.chamoparty.velocity.messaging.VelocityToBackend;

public class VotifierListener {

    private final VelocityToBackend backend;
    private final ChamoPartyVelo plugin;

    public VotifierListener(VelocityToBackend backend, ChamoPartyVelo plugin) {
        this.backend = backend;
        this.plugin = plugin;
    }

    @Subscribe
    public void onVote(VotifierEvent event) {
        //String voteData
        //backend.sendPluginMessageToAllBackends(VOTE, voteData.getBytes(StandardCharsets.UTF_8));
    }
}
