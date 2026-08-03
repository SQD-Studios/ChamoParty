package net.chamosmp.chamoparty.paper.core.utils.plugins;

import net.chamosmp.chamoparty.api.enums.Message;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.sched.SchedulerUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.chamosmp.chamoparty.paper.core.logger.Logger.LogType.WARNING;


public class VersionChecker implements Listener {

    private final String URL_RESOURCE = "https://modrinth.com/plugin/chamoparty";
    private final Plugin plugin;

    /**
     * Class constructor
     *
     * @param plugin
     */
    public VersionChecker(Plugin plugin) {
        super();
        this.plugin = plugin;
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) throws IOException, InterruptedException {
        final Player player = event.getPlayer();
        if (isNewerVersion(plugin.getPluginMeta().getVersion(), remoteVer()) && event.getPlayer().hasPermission("chamoparty.update")) {
            SchedulerUtil.runAtEntityLater(plugin, player, () -> {
                Component prefix = Message.PREFIX.getMessage();
                player.sendRichMessage(prefix +
                        """
                                <red>You do not use the latest version of the plugin! Update to eliminate the risk of problems!
                                <white>Download plugin here: <green>"
                                """ + URL_RESOURCE);
            }, () -> {
            }, 20 * 2);
        }
    }

    public void modrinthVersionCheck() throws Exception {
        String pluginVer = plugin.getPluginMeta().getVersion();
        String version = remoteVer();

        if (!version.equals("failed")) {
            if (isNewerVersion(pluginVer, version)) {
                Logger.log("New update available. Your version: " + pluginVer + ", latest version: " + version);
                Logger.log("Download plugin here: " + URL_RESOURCE);
            } else {
                Logger.log("No update available.");
            }
        } else {
            Logger.log("Failed to check for updates.", WARNING);
        }
    }

    public String remoteVer() throws IOException, InterruptedException {
        String baseUrl = "https://api.modrinth.com/v2";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/project/V5rKW5Zq/version"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        Pattern pattern = Pattern.compile("\"version_number\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(responseBody);

        if (!matcher.find()) {
            return "failed";
        }

        return matcher.group(1);

    }

    public static boolean isNewerVersion(String current, String latest) {
        String[] currentParts = current.split("\\.");
        String[] latestParts = latest.split("\\.");

        int maxLength = Math.max(currentParts.length, latestParts.length);

        for (int i = 0; i < maxLength; i++) {

            int currentValue =
                    i < currentParts.length
                            ? Integer.parseInt(currentParts[i])
                            : 0;

            int latestValue =
                    i < latestParts.length
                            ? Integer.parseInt(latestParts[i])
                            : 0;

            if (latestValue > currentValue) {
                return true;
            }

            if (latestValue < currentValue) {
                return false;
            }
        }

        return false;
    }

}
