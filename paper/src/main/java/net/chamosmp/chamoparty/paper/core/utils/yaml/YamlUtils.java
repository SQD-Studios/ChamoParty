package net.chamosmp.chamoparty.paper.core.utils.yaml;

import net.chamosmp.chamoparty.paper.core.utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class YamlUtils extends Utils {

    protected final JavaPlugin plugin;

    /**
     * @param plugin
     */
    public YamlUtils(JavaPlugin plugin) {
        super();
        this.plugin = plugin;
    }


    /**
     * Get config
     *
     * @param path
     * @return {@link YamlConfiguration}
     */
    protected YamlConfiguration getConfig(File file) {
        if (file == null)
            return null;
        return YamlConfiguration.loadConfiguration(file);
    }

}
