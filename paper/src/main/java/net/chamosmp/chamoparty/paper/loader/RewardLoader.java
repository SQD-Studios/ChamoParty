package net.chamosmp.chamoparty.paper.loader;

import net.chamosmp.chamoparty.paper.core.utils.loader.Loader;
import net.chamosmp.chamoparty.paper.implementations.ChamoReward;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class RewardLoader implements Loader<net.chamosmp.chamoparty.paper.api.Reward> {

    @Override
    public net.chamosmp.chamoparty.paper.api.Reward load(YamlConfiguration configuration, String path, Object... args) {

        double percent = configuration.getDouble(path + "percent", 10);
        List<String> commands = configuration.getStringList(path + "commands");
        boolean needToBeOnline = configuration.getBoolean(path + "needToBeOnline", false);
        List<String> messages = configuration.getStringList(path + "broadcast");

        return new ChamoReward(percent, commands, needToBeOnline, messages);
    }

    @Override
    public void save(net.chamosmp.chamoparty.paper.api.Reward object, YamlConfiguration configuration, String path) {
        // TODO Auto-generated method stub

    }

}