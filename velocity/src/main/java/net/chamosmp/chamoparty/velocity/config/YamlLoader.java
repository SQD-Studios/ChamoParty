package net.chamosmp.chamoparty.velocity.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class YamlLoader {

    private CommentedConfigurationNode configNode;

    public void loadConfig(Path path) throws IOException {
        Files.createDirectories(path.getParent());

        URL bundled = getClass().getClassLoader().getResource(path.toFile().getName());
        if (bundled == null) {
            return;
        }
        YamlConfigurationLoader bundledLoader = YamlConfigurationLoader.builder()
                .url(bundled)
                .build();
        CommentedConfigurationNode bundledNode = bundledLoader.load();

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(path)
                .build();
        CommentedConfigurationNode configNode = loader.load();


        if (!Files.exists(path)) {
            loader.save(bundledNode);
            this.configNode = configNode;
            return;

        }
        CommentedConfigurationNode userNode = loader.load();
        mergeDefaults(userNode, bundledNode);
        loader.save(userNode);

        this.configNode = configNode;
    }

    public CommentedConfigurationNode getConfigNode() {
        return configNode;
    }

    private void mergeDefaults(CommentedConfigurationNode userNode, CommentedConfigurationNode defaultNode) throws SerializationException {
        for (Object key : defaultNode.childrenMap().keySet()) {
            if (userNode.node(key).virtual()) {
                // Copy the default value
                Object defaultValue = defaultNode.node(key).get(Object.class);
                userNode.node(key).set(defaultValue);
            }
        }
    }
}
