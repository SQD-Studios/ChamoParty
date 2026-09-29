package net.chamosmp.chamoparty.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.chamomsp.sqdlib.velocity.VelocityPlugin;
import net.chamomsp.sqdlib.velocity.util.ConfigUtil;
import net.chamomsp.sqdlib.velocity.util.LoggerUtil;
import net.chamosmp.chamoparty.velocity.commands.BaseCommandBrigadier;
import net.chamosmp.chamoparty.velocity.listener.VotifierListener;
import net.chamosmp.chamoparty.velocity.messaging.BackendToVelocity;
import net.chamosmp.chamoparty.velocity.messaging.VelocityToBackend;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.nio.file.Path;

import static net.chamosmp.chamoparty.velocity.messaging.BackendToVelocity.VOTE;


public class ChamoPartyVelo extends VelocityPlugin {
    public final Path pluginFolderPath;
    public final Path configPath;
    private final ProxyServer server;
    private final Logger logger;
    private final Metrics.Factory metricsFactory;
    private VotifierListener votifierListener;

    @Inject
    public ChamoPartyVelo(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        super(server, dataDirectory);

        this.server = server;
        this.logger = logger;
        this.pluginFolderPath = dataDirectory;
        this.metricsFactory = metricsFactory;
        this.configPath = dataDirectory.resolve("config.yml");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        new LoggerUtil("<aqua>chamoParty</aqua>| ", server);

        ConfigUtil.loadOrAdapt(this, "config.yml");

        initInstances();

        server.getEventManager().register(this, votifierListener);

        metricsFactory.make(this, 33142);
        logger.info("Enabled metrics");

        registerChannels();

        // Registering the commands
        try {
            BaseCommandBrigadier.register(this.server, this, this);
            logger.info("Registered commands");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Offline mode message
        if (!server.getConfiguration().isOnlineMode()) {
            logger.warn("""
                    It appears that you are running an offline mode server. We, do not provide support for setups that bypass Mojang's authentication.
                    You are on your own to solve any issues that arise.""");
        }
    }

    public void registerChannels() {
        server.getChannelRegistrar().register(VOTE);
        logger.info("Registered plugin messaging channels");
    }

    public void initInstances() {
        VelocityToBackend velocityToBackend = new VelocityToBackend(server);
        BackendToVelocity backendToVelocity = new BackendToVelocity(velocityToBackend, server);
        votifierListener = new VotifierListener(velocityToBackend, this);
    }
}
