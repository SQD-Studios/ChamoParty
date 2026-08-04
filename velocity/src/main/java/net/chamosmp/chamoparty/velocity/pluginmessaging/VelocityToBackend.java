package net.chamosmp.chamoparty.velocity.pluginmessaging;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.Optional;

public class VelocityToBackend {
    public boolean sendPluginMessageToBackend(RegisteredServer server, ChannelIdentifier identifier, byte[] data) {
        return server.sendPluginMessage(identifier, data);
    }

    public boolean sendPluginMessageToBackendUsingPlayer(Player player, ChannelIdentifier identifier, byte[] data) {
        Optional<ServerConnection> connection = player.getCurrentServer();
        return connection.map(serverConnection -> serverConnection.sendPluginMessage(identifier, data)).orElse(false);
    }
}
