package net.chamosmp.chamoparty.velocity.pluginmessaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

public class BackendToVelocity {
    public static final MinecraftChannelIdentifier VOTE = MinecraftChannelIdentifier.from("chamoparty:votifiersendvote");

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

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
    }
}
