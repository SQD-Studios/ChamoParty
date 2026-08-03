package net.chamosmp.chamoparty.paper.api.enums;

import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.save.Config;
import org.bukkit.Material;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public enum Options {

    ENABLE_DEBUG("enableDebug", "Enables the debug mode of the plugin.", "You will thus be able to obtain information", "in your console."),

    ENABLE_DEBUG_TIME("enableDebugTime", "Enable debug time of the plugin", "This is a debugging tool."),

    ENABLE_LOG_MESSAGE("enableLogMessage", "Enable log message in console", "This is a debugging tool."),

    ENABLE_VERSION_CHECKER("enableAutoUpdate", "Enable plugin version checker."),

    ENABLE_INVENTORY_PRE_RENDER("enableInventoryPreRender", "Allows you to make items that are permanent."),

    ENABLE_OPEN_SYNC_INVENTORY("enableOpenSyncInventory", "Allows to open the inventory with the items in a synchronized way."),

    ENABLE_VOTE_COMMAND("enableVoteCommand", "Allows you to activate the /vote command."),

    ENABLE_VOTE_INVENTORY("enableVoteInventory", "Allows you to open the inventory with the /vote command."),

    ENABLE_VOTE_MESSAGE("enableVoteMessage", "Allows you to display the message in the /vote command."),

    ENABLE_ACTION_BAR_BROADCAST("enableActionBarVoteAnnonce", "Allows you to activate the bar action when a player votes."),

    ENABLE_TCHAT_BROADCAST("enableTchatVoteAnnonce", "Allows you to activate the broadcast message when a player votes."),

    ;

    private final String fieldName;
    private final List<String> descriptions;

    /**
     * @param name
     * @param strings
     */
    Options(String name, String... strings) {
        this.fieldName = name;
        this.descriptions = Arrays.asList(strings);
    }

    /**
     * @return the name
     */
    public String getName() {
        return fieldName;
    }

    /**
     * @return the descriptions
     */
    public List<String> getDescriptions() {
        return descriptions;
    }

    public void toggle(ChamoPartyPlugin plugin) {
        try {
            Class<Config> configClass = Config.class;
            Field field = configClass.getDeclaredField(fieldName);
            field.set(configClass, !(Boolean) field.get(configClass));

            Config.getInstance().save(plugin.getPersist());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isToggle() {

        try {
            Class<Config> configClass = Config.class;
            Field field = configClass.getDeclaredField(fieldName);
            return (Boolean) field.get(configClass);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
