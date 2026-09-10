package net.chamosmp.chamoparty.velocity.messaging;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class VelocityToBackend {

    private final ProxyServer server;

    public VelocityToBackend(ProxyServer server) {
        this.server = server;
    }

    public void sendPluginMessageToBackend(RegisteredServer server, ChannelIdentifier identifier, byte[] data) {
        server.sendPluginMessage(identifier, data);
    }

    public void sendPluginMessageToAllBackends(ChannelIdentifier identifier, byte[] data) {
        server.getAllServers().forEach(server -> {
            sendPluginMessageToBackend(server, identifier, data);
        });
    }
}