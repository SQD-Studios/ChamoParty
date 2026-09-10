package net.chamosmp.chamoparty.velocity.messaging;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

public class BackendToVelocity {
    public static final MinecraftChannelIdentifier VOTE = MinecraftChannelIdentifier.from("chamoparty:chamoparty");

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

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }

        velocityToBackend.sendPluginMessageToAllBackends(VOTE, event.getData());
    }
}