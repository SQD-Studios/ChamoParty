package net.chamosmp.chamoparty.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.vexsoftware.votifier.velocity.event.VotifierEvent;
import net.chamosmp.chamoparty.velocity.config.YamlLoader;
import net.chamosmp.chamoparty.velocity.messaging.VelocityToBackend;

import java.nio.charset.StandardCharsets;

import static net.chamosmp.chamoparty.velocity.messaging.BackendToVelocity.VOTE;

public class VotifierListener {

    private final YamlLoader loader;
    private final VelocityToBackend backend;

    public VotifierListener(YamlLoader loader, VelocityToBackend backend) {
        this.loader = loader;
        this.backend = backend;
    }

    @Subscribe
    public void onVote(VotifierEvent event) {
        //String voteData
        //backend.sendPluginMessageToAllBackends(VOTE, voteData.getBytes(StandardCharsets.UTF_8));
    }
}
