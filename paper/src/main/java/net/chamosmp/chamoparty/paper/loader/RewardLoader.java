package net.chamosmp.chamoparty.paper.loader;

import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.impl.ChamoReward;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public final class RewardLoader {
    public static Reward load(YamlConfiguration configuration, String path) {

        double percent = configuration.getDouble(path + "percent", 10);
        List<String> commands = configuration.getStringList(path + "commands");
        boolean needToBeOnline = configuration.getBoolean(path + "needToBeOnline", false);
        List<String> messages = configuration.getStringList(path + "broadcast");

        return new ChamoReward(percent, commands, needToBeOnline, messages);
    }
}