package net.chamosmp.chamoparty.zcore.utils.plugins;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.chamosmp.chamoparty.ZVotePartyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import net.chamosmp.chamoparty.api.enums.Message;
import net.chamosmp.chamoparty.zcore.logger.Logger;

/**
 * 
 * @author Maxlego08
 * @author Chamogelastos
 *
 */
public class VersionChecker implements Listener {

	private final String URL_API = "https://groupez.dev/api/v1/resource/version/%s";
	private final String URL_RESOURCE = "https://groupez.dev/resources/%s";
	private final Plugin plugin;
	private final int pluginID;
	private boolean useLastVersion = false;

	/**
	 * Class constructor
	 * 
	 * @param plugin
	 * @param pluginID
	 */
	public VersionChecker(Plugin plugin, int pluginID) {
		super();
		this.plugin = plugin;
		this.pluginID = pluginID;
	}

	/**
	 * Allows to check if the plugin version is up to date.
	 * @deprecated Please use {@link VersionChecker#modrinthVersionCheck()}. It is a direct replacement and should work as this was
	 */
	@Deprecated(since = "0.0.1", forRemoval = true)
	public void useLastVersion() {

		Bukkit.getPluginManager().registerEvents(this, this.plugin); // Register
																		// event

		String pluginVersion = plugin.getPluginMeta().getVersion();
		AtomicBoolean atomicBoolean = new AtomicBoolean();
		this.getVersion(version -> {

			long ver = Long.valueOf(version.replace(".", ""));
			long plVersion = Long.valueOf(pluginVersion.replace(".", ""));
			atomicBoolean.set(plVersion >= ver);
			this.useLastVersion = atomicBoolean.get();
			if (atomicBoolean.get())
				Logger.info("No update available.");
			else {
				Logger.info("New update available. Your version: " + pluginVersion + ", latest version: " + version);
				Logger.info("Download plugin here: " + String.format(URL_RESOURCE, this.pluginID));
			}
		});

	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) throws IOException, InterruptedException {
		final Player player = event.getPlayer();
		if (isNewerVersion(plugin.getPluginMeta().getVersion(), remoteVer()) && event.getPlayer().hasPermission("zplugin.notifs")) {
			ZVotePartyPlugin.getScheduler().runAtEntityLater(player, () -> {
				String prefix = Message.PREFIX.getMessage();
				player.sendMessage(prefix
						+ "§cYou do not use the latest version of the plugin! Thank you for taking the latest version to avoid any risk of problem!");
				player.sendMessage(prefix + "§fDownload plugin here: §a" + String.format(URL_RESOURCE, pluginID));

			}, 20 * 2);
		}
	}

	/**
	 * Get version by plugin id
	 * 
	 * @param consumer
	 *            - Do something after
	 * @deprecated This uses the groupez resource to check for the update.
	 */
	@Deprecated(since = "0.0.1", forRemoval = true)
	public void getVersion(Consumer<String> consumer) {
		ZVotePartyPlugin.getScheduler().runAsync(task -> {
			final String apiURL = String.format(URL_API, this.pluginID);
			try {
				URI uri = new URI(apiURL);
				URL url = uri.toURL();
				URLConnection hc = url.openConnection();
				hc.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
				Scanner scanner = new Scanner(hc.getInputStream());
				if (scanner.hasNext())
					consumer.accept(scanner.next());
				scanner.close();

			} catch (IOException exception) {
				this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
			} catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
	}

	public void modrinthVersionCheck() throws Exception {
		String pluginVer = plugin.getPluginMeta().getVersion();
		String version = remoteVer();

		if (!version.equals("failed")) {
		if (isNewerVersion(pluginVer, version)) {
			Logger.info("New update available. Your version: " + pluginVer + ", latest version: " + version);
			Logger.info("Download plugin here: " + String.format(URL_RESOURCE, this.pluginID));
		} else {
			Logger.info("No update available.");
		}
		} else  {
			Logger.info("Failed to check for updates.");
		}
	}
	public String remoteVer() throws IOException, InterruptedException {
		String pluginVer = plugin.getPluginMeta().getVersion();
		String baseUrl = "https://api.modrinth.com/v2";

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/project/V5rKW5Zq/version"))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		String responseBody = response.body();

		// Parse version_number from JSON using regex — no ObjectMapper needed
		Pattern pattern = Pattern.compile("\"version_number\"\\s*:\\s*\"([^\"]+)\"");
		Matcher matcher = pattern.matcher(responseBody);

		if (!matcher.find()) {
			Logger.info("Cannot look for updates: ");
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
