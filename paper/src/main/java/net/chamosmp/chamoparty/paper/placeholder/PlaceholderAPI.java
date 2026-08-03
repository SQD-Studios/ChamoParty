package net.chamosmp.chamoparty.paper.placeholder;

import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import net.chamosmp.chamoparty.paper.save.Config;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderAPI extends Utils {

    private ChamoPartyPlugin plugin;
    private final String prefix = "chamoparty";
    private final Pattern pattern = Pattern.compile("[%]([^%]+)[%]");

    /**
     * Set plugin instance
     *
     * @param plugin
     */
    public void setPlugin(ChamoPartyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * static Singleton instance.
     */
    private static volatile PlaceholderAPI instance;

    /**
     * Private constructor for singleton.
     */
    private PlaceholderAPI() {
    }

    /**
     * Return a singleton instance of ZPlaceholderApi.
     */
    public static PlaceholderAPI getInstance() {
        // Double lock for thread safety.
        if (instance == null) {
            synchronized (PlaceholderAPI.class) {
                if (instance == null) {
                    instance = new PlaceholderAPI();
                }
            }
        }
        return instance;
    }

    /**
     *
     * @param player
     * @param displayName
     * @return
     */
    public String setPlaceholders(Player player, String placeholder) {

        if (placeholder == null || !placeholder.contains("%")) {
            return placeholder;
        }

        final String realPrefix = this.prefix + "_";

        Matcher matcher = this.pattern.matcher(placeholder);
        while (matcher.find()) {
            String stringPlaceholder = matcher.group(0);
            String regex = matcher.group(1).replace(realPrefix, "");
            String replace = this.onRequest(player, regex);
            if (replace != null) {
                placeholder = placeholder.replace(stringPlaceholder, replace);
            }
        }

        return placeholder;
    }

    /**
     * Custom placeholder
     *
     * @param player
     * @param string
     * @return
     */
    public String onRequest(Player player, String string) {
        VotePartyManager manager = plugin.getManager();
        IStorage iStorage = plugin.getIStorage();

        return switch (string) {
            case "votes_recorded" -> String.valueOf(iStorage.getVoteCount());
            case "votes_required_party" -> String.valueOf(manager.getNeedVotes() - iStorage.getVoteCount());
            case "votes_required_total" -> String.valueOf(manager.getNeedVotes());
            case "votes_progressbar" ->
                    this.getProgressBar(iStorage.getVoteCount(), manager.getNeedVotes(), Config.progressBar);
            case "player_votes" -> player == null ? null : String.valueOf(manager.getPlayerVoteCount(player));
            default -> null;
        };

    }

}
