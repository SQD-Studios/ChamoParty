package net.chamosmp.chamoparty.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.chamosmp.chamoparty.velocity.commands.BaseBrigadier;
import net.chamosmp.chamoparty.velocity.config.YamlLoader;
import net.chamosmp.chamoparty.velocity.messaging.BackendToVelocity;
import net.chamosmp.chamoparty.velocity.messaging.VelocityToBackend;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;


public class ChamoPartyVelo {
    private final ProxyServer server;
    private final Logger logger;
    public final Path pluginFolderPath;
    private final Metrics.Factory metricsFactory;
    public final Path configPath;

    private YamlLoader yamlLoader;

    @Inject
    public ChamoPartyVelo(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        this.server = server;
        this.logger = logger;
        this.pluginFolderPath = dataDirectory;
        this.metricsFactory = metricsFactory;
        this.configPath = dataDirectory.resolve("config.yml");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        initInstances();

        Metrics metrics = metricsFactory.make(this, 33142);
        logger.info("Enabled metrics");

        registerChannels();

        // Registering the commands
        BaseBrigadier.register(this.server, this, yamlLoader, this);
        logger.info("Registered commands");


        if (configPath != null) {
            try {
                yamlLoader.loadConfig(configPath);
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

    public void registerChannels() {
        server.getChannelRegistrar().register(new BackendToVelocity(new VelocityToBackend(), server).VOTE);
        logger.info("Registered plugin messaging channels");
    }

    public void initInstances() {
        this.yamlLoader = new YamlLoader(this);
    }

    public Logger getLogger() {
        return logger;
    }
}
