package net.chamosmp.chamoparty.paper.save;

import net.chamosmp.chamoparty.api.enums.Message;
import net.chamosmp.chamoparty.api.enums.MessageType;
import net.chamosmp.chamoparty.paper.core.utils.storage.Saveable;
import net.chamosmp.chamoparty.paper.core.utils.yaml.YamlUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageLoader extends YamlUtils implements Saveable {

    public MessageLoader(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void save() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        YamlConfiguration configuration = getConfig(file);
        for (Message message : Message.values()) {

            if (!message.isUse())
                continue;

            String path = "messages." + message.name().toLowerCase().replace("_", ".");

            configuration.set(path + ".type", message.getType().name());

            if (message.getType().equals(MessageType.TCHAT) || message.getType().equals(MessageType.ACTION)) {

                configuration.set(path + ".message", message.getString());

            } else if (message.getType().equals(MessageType.TITLE)) {

                configuration.set(path + ".title", colorReverse(message.getTitle()));
                configuration.set(path + ".subtitle", colorReverse(message.getSubTitle()));
                configuration.set(path + ".fadeInTime", message.getStart());
                configuration.set(path + ".showTime", message.getTime());
                configuration.set(path + ".fadeOutTime", message.getEnd());

            }

        }

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void load() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            this.save();
            return;
        }

        YamlConfiguration configuration = getConfig(file);

        if (!configuration.contains("messages")) {
            this.save();
            return;
        }

        for (String key : configuration.getConfigurationSection("messages.").getKeys(false)) {
            loadMessage(configuration, "messages." + key);
        }

        // Pour avoir directs les news param§tres
        this.save();
    }

    /**
     *
     * @param configuration
     * @param key
     */
    private void loadMessage(@NonNull YamlConfiguration configuration, String key) {
        if (configuration.contains(key + ".type")) {
            MessageType messageType = MessageType.valueOf(configuration.getString(key + ".type").toUpperCase());
            String keys = key.substring("messages.".length());
            Message enumMessage = Message.valueOf(keys.toUpperCase().replace(".", "_"));
            enumMessage.setType(messageType);
            switch (messageType) {
                case ACTION, TCHAT: {
                    String message = configuration.getString(key + ".message");
                    enumMessage.setMessage(color(message));
                    break;
                }
                case TITLE: {
                    String title = configuration.getString(key + ".title");
                    String subtitle = configuration.getString(key + ".subtitle");
                    int fadeInTime = configuration.getInt(key + ".fadeInTime");
                    int showTime = configuration.getInt(key + ".showTime");
                    int fadeOutTime = configuration.getInt(key + ".fadeOutTime");
                    Map<String, Object> titles = new HashMap<String, Object>();
                    titles.put("title", color(title));
                    titles.put("subtitle", color(subtitle));
                    titles.put("start", fadeInTime);
                    titles.put("time", showTime);
                    titles.put("end", fadeOutTime);
                    titles.put("isUse", true);
                    enumMessage.setTitles(titles);
                    break;
                }
                default:
                    break;
            }

            return;
        }

        for (String newKey : configuration.getConfigurationSection(key + ".").getKeys(false))
            loadMessage(configuration, key + "." + newKey);
    }

}