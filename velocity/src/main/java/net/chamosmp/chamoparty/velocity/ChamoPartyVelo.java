package net.chamosmp.chamoparty.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.chamosmp.chamoparty.velocity.config.YamlLoader;
import net.chamosmp.chamoparty.velocity.pluginmessaging.BackendToVelocity;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;


public class ChamoPartyVelo {
    private final ProxyServer server;
    private final Logger logger;
    private final Path configPath;
    private final Metrics.Factory metricsFactory;

    private YamlLoader yamlLoader;

    @Inject
    public ChamoPartyVelo(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        this.server = server;
        this.logger = logger;
        this.configPath = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        initInstances();

        int pluginId = 33142;
        Metrics metrics = metricsFactory.make(this, pluginId);
        logger.info("Enabled metrics");

        registerChannels();

        if (configPath != null) {
            try {
                yamlLoader.loadConfig(configPath.resolve("config.yml"));
                logger.info("Loaded config files");
            } catch (IOException e) {
                logger.error("Failed to load config!", e);
            }
        } else {
            logger.error("Failed to load config! Path to plugin is null!");
        }

        // Offline mode message
        if (!server.getConfiguration().isOnlineMode()) {
            logger.warn("""
                    It appears that you are running an offline mode server. We, do not provide support for setups that bypass Mojang's authentication.
                    You are on your own to solve any issues that arise.""");
        }
    }


    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {

    }

    public void registerChannels() {
        server.getChannelRegistrar().register(BackendToVelocity.VOTE);
        logger.info("Registered plugin messaging channels");
    }

    public void initInstances() {
        this.yamlLoader = new YamlLoader();
    }

}
