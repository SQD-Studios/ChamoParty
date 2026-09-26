package net.chamosmp.chamoparty.paper;

import io.papermc.paper.util.Tick;
import net.chamosmp.chamoparty.api.enums.Message;
import net.chamosmp.chamoparty.api.enums.RewardType;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.api.Vote;
import net.chamosmp.chamoparty.paper.api.VotePartyManager;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.loader.RewardLoader;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import net.chamosmp.chamoparty.paper.utils.MessageUtils;
import net.chamosmp.sqdlib.paper.util.ConfigUtil;
import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import net.chamosmp.sqdlib.paper.util.SchedulerUtil;
import net.chamosmp.sqdlib.util.LogType;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChamoPartyManager extends MessageUtils implements VotePartyManager {

    private final ChamoPartyPlugin plugin;
    private final List<Reward> rewards = new ArrayList<>();
    private final List<Reward> partyRewards = new ArrayList<>();
    private List<String> globalCommands = new ArrayList<>();
    private List<String> commands = new ArrayList<>();
    private long needVote = 50;
    private String partySound;

    public ChamoPartyManager(ChamoPartyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void reload(CommandSender sender) {
        try {
            this.plugin.reloadConfig();
            this.loadConfiguration();
            this.plugin.getMessageLoader().load();
            this.plugin.reloadInventories();
            LegacyJsonConfig.reloadConfigSafely(plugin);
            super.message(sender, Message.RELOAD_SUCCESS);
        } catch (Exception e) {
            MessageUtils.message(sender, Message.RELOAD_ERROR.getMessage() + e.getMessage());
        }
    }

    @Override
    public void loadConfiguration() {
        YamlConfiguration configuration = ConfigUtil.loadOrAdapt(plugin, "config.yml", List.of("rewards.", "party."));
        ConfigurationSection configurationSection;

        this.rewards.clear();
        try {
            configurationSection = configuration.getConfigurationSection("rewards.");
            if (configurationSection != null) {
                for (String key : configurationSection.getKeys(false)) {
                    String path = "rewards." + key + ".";
                    Reward reward = RewardLoader.load(configuration, path);
                    this.rewards.add(reward);
                }
            }
        } catch (Exception ignored) {
        }

        LoggerUtil.log(LogType.INFO, "Loaded " + this.rewards.size() + " rewards");

        this.needVote = configuration.getLong("party.votes_needed", 50);
        this.globalCommands = configuration.getStringList("party.global_commands");
        this.commands = configuration.getStringList("party.commands");
        this.partySound = configuration.getString("party.Sound", "");

        this.partyRewards.clear();
        if (configuration.isConfigurationSection("party.rewards.")) {
            try {
                configurationSection = configuration.getConfigurationSection("party.rewards.");
                if (configurationSection == null) {
                    return;
                }
                for (String key : configurationSection.getKeys(false)) {
                    String path = "party.rewards." + key + ".";
                    Reward reward = RewardLoader.load(configuration, path);
                    this.partyRewards.add(reward);
                }
            } catch (Exception _) {
            }
        }
    }

    @Override
    public void openVote(Player player) {
        if (LegacyJsonConfig.enableVoteInventory && this.plugin.getLoader() != null) {
            this.plugin.getLoader().open(player);
            return;
        }
        if (!LegacyJsonConfig.enableVoteMessage)
            message(player, "§cFound error in the vote party plugin configuration, please contact an administrator.");
    }

    @Override
    public void vote(String username, String serviceName, boolean updateVoteParty) {
        OfflinePlayer offlinePlayer = Bukkit.getPlayerExact(username);
        if (offlinePlayer == null) offlinePlayer = Bukkit.getOfflinePlayer(username);

        this.handleVoteParty();

        this.vote(offlinePlayer, serviceName);
    }

    @Override
    public void handleVoteParty() {
        IStorage iStorage = this.plugin.getIStorage();
        iStorage.addVoteCount(1);
        if (iStorage.getVoteCount() >= this.needVote) this.start();
    }

    @Override
    public void vote(CommandSender sender, String username, boolean updateVoteParty) {
        this.vote(username, "ChamoParty", updateVoteParty);
        message(sender, Message.VOTE_SEND, Map.of("player", username));
    }

    @Override
    public void vote(OfflinePlayer offlinePlayer, String serviceName) {
        Reward reward = getRandomReward(RewardType.VOTE);
        if (reward == null) return;

        IStorage iStorage = this.plugin.getIStorage();

        this.plugin.get(offlinePlayer, playerVote -> {
            Vote vote = playerVote.vote(this.plugin, serviceName, reward, false);
            iStorage.insertVote(playerVote, vote, reward);
        });
    }

    @Override
    public void secretStart() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            boolean eligible = true;
            // Check if only voters should receive rewards
            if (LegacyJsonConfig.only_voters_rewards) {
                long votes = this.plugin.getPlayerManager().getSyncPlayer(player)
                        .map(PlayerVote::getVoteCount)
                        .orElse(0);
                if (votes == 0) eligible = false;
            }

            if (eligible) {
                // Rewards and global commands for eligible players
                SchedulerUtil.runDelayed(plugin, () ->
                        this.globalCommands.forEach(command -> {
                            if (command == null || command.trim().isEmpty()) return;
                            command = command.replace("%player%", player.getName());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.papi(command, player));
                        }), 1L
                );

                if (!this.partySound.isEmpty()) {
                    try {
                        String[] parts = this.partySound.split(":");
                        String soundName = parts[0].toUpperCase();
                        float volume = parts.length > 1 ? Float.parseFloat(parts[1]) : 1.0f;
                        float pitch = parts.length > 2 ? Float.parseFloat(parts[2]) : 1.0f;
                        Sound sound = Registry.SOUNDS.get(NamespacedKey.minecraft(soundName.toLowerCase()));
                        if (sound != null) {
                            player.playSound(player.getLocation(), sound, volume, pitch);
                        }
                    } catch (Exception ignored) {
                    }
                }

                Reward reward = getRandomReward(RewardType.VOTE_PARTY);
                if (reward != null) reward.give(this.plugin, player);
            } else { // When a player is not eligible, send a message and play a sound
                message(player, Message.NOT_ELIGIBLE_PARTY);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
            }
        }

        // Execute party commands for all eligible players
        SchedulerUtil.runDelayed(plugin, () ->
                this.commands.forEach(command -> {
                    if (command == null || command.trim().isEmpty()) return;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }), 1L
        );

        broadcast(Message.VOTE_PARTY_START, Map.of());
    }

    @Override
    public @Nullable Reward getRandomReward(RewardType type) {
        List<Reward> rewardList = type == RewardType.VOTE ? this.rewards : this.partyRewards;
        if (rewardList.isEmpty()) return null;

        int attempts = 0;
        Reward selected = null;
        while (attempts < 10) {
            Reward reward = rewardList.get(ThreadLocalRandom.current().nextInt(rewardList.size()));
            double chance = ThreadLocalRandom.current().nextDouble(0, 100);
            if (reward.getPercent() >= 100 || reward.getPercent() >= chance) {
                selected = reward;
                break;
            }
            attempts++;
        }
        return selected != null ? selected : rewardList.get(ThreadLocalRandom.current().nextInt(rewardList.size()));
    }

    @Override
    public void giveVotes(Player player) {
        this.plugin.get(player, playerVote -> {
            List<Vote> votes = playerVote.getNeedRewardVotes();
            if (!votes.isEmpty()) {
                SchedulerUtil.runDelayed(plugin, () -> {
                    message(player, Message.VOTE_LATER, Map.of("amount", votes.size()));
                    votes.forEach(e -> e.giveReward(this.plugin, player));
                }, Tick.tick().fromDuration(Duration.ofMillis(LegacyJsonConfig.joinGiveVoteMilliSecond)));
                this.plugin.getIStorage().updateRewards(player.getUniqueId());
            }
        });
    }

    @Override
    public List<String> getGlobalCommands() {
        return this.globalCommands;
    }

    @Override
    public List<Reward> getPartyReward() {
        return this.partyRewards;
    }

    @Override
    public long getNeedVotes() {
        return this.needVote;
    }

    @Override
    public long getPlayerVoteCount(Player player) {
        Optional<PlayerVote> optional = this.plugin.getPlayerManager().getSyncPlayer(player);
        return optional.map(PlayerVote::getVoteCount).orElse(0);
    }

    @Override
    public void sendNeedVote(CommandSender sender) {
        message(sender, Message.VOTE_NEEDED);
    }

    @Override
    public void forceStart(CommandSender sender) {
        message(sender, Message.VOTE_STARTPARTY);
        this.start();
    }

    @Override
    public void start() {
        this.plugin.getIStorage().startVoteParty();
        this.secretStart();
    }

    @Override
    public void removeVote(CommandSender sender, OfflinePlayer player) {
        this.plugin.getPlayerManager().getPlayer(player, optional -> {
            if (optional.isEmpty() || optional.get().getVoteCount() == 0) {
                message(sender, Message.VOTE_REMOVE_ERROR, Map.of("player", player.getName()));
                return;
            }
            PlayerVote playerVote = optional.get();
            playerVote.removeVote();
            message(sender, Message.VOTE_REMOVE_SUCCESS, Map.of("player", player.getName()));
        }, true);
    }

    @Override
    public void voteOffline(UUID uniqueId, String serviceName) {
        Reward reward = getRandomReward(RewardType.VOTE);
        if (reward == null) return;

        this.plugin.get(uniqueId, playerVote -> {
            Vote vote = playerVote.vote(this.plugin, serviceName, reward, true);
            this.plugin.getIStorage().insertVote(playerVote, vote, reward);
        });
    }
}