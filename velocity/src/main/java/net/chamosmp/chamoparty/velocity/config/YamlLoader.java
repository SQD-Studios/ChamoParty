package net.chamosmp.chamoparty.velocity.config;

import net.chamosmp.chamoparty.velocity.ChamoPartyVelo;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class YamlLoader {

    private CommentedConfigurationNode configNode;
    private Path configFilePath;

    private final ChamoPartyVelo plugin;

    public YamlLoader(ChamoPartyVelo plugin) {
        this.plugin = plugin;
    }

    public void loadConfig(Path path) throws IOException {
        this.configFilePath = path;
        Files.createDirectories(path.getParent());
        URL bundledUrl = getClass().getClassLoader().getResource("config.yml");

        YamlConfigurationLoader bundledLoader = YamlConfigurationLoader.builder()
                .url(bundledUrl)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
        CommentedConfigurationNode bundledNode = bundledLoader.load();

        YamlConfigurationLoader fileLoader = YamlConfigurationLoader.builder()
                .path(path)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();

        if (!Files.exists(path)) {
            System.out.println("Saving config to: " + path.toAbsolutePath());
            try {
                fileLoader.save(bundledNode);
            } catch (Exception e) {
                System.err.println("Save failed!");
                e.printStackTrace();
                throw e;
            }
            System.out.println("Config saved successfully.");

            // Verify
            if (Files.exists(path)) {
                System.out.println("✅ File exists immediately after save.");
            } else {
                System.err.println("❌ File does NOT exist after save!");
            }
            this.configNode = bundledNode;
            return;
        }

        CommentedConfigurationNode userNode = fileLoader.load();
        mergeDefaults(userNode, bundledNode);
        fileLoader.save(userNode);
        this.configNode = userNode;
    }

    public CommentedConfigurationNode getConfigNode() {
        return configNode;
    }

    private void mergeDefaults(CommentedConfigurationNode userNode, CommentedConfigurationNode defaultNode) throws SerializationException {
        for (Object key : defaultNode.childrenMap().keySet()) {
            if (userNode.node(key).virtual()) {
                Object defaultValue = defaultNode.node(key).get(Object.class);
                userNode.node(key).set(defaultValue);
            }
        }
    }

    public void reloadConfig() throws ConfigurateException {
        YamlConfigurationLoader fileLoader = YamlConfigurationLoader.builder()
                .path(configFilePath)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
        fileLoader.save(this.configNode);
        this.configNode = fileLoader.load();
    }
}
