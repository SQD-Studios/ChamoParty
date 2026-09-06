package net.chamosmp.chamoparty.velocity.messaging;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class BackendToVelocity {
    public static final MinecraftChannelIdentifier VOTE = MinecraftChannelIdentifier.from("chamoparty:votifiersendvote");

    private final VelocityToBackend velocityToBackend;
    private final ProxyServer proxyServer;

    public BackendToVelocity(VelocityToBackend velocityToBackend, ProxyServer proxyServer) {
        this.velocityToBackend = velocityToBackend;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onPluginMessageFromBackend(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(VOTE)) {
            return;
        }

        // mark PluginMessage as handled, indicating that the contents
        // should not be forwarding to their original destination.
        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection backend)) {
            return;
        }

        for (RegisteredServer server : proxyServer.getAllServers()) {
            velocityToBackend.sendPluginMessageToBackend(server, VOTE, event.getData());
        }
    }
}
