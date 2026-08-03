package net.chamosmp.chamoparty.paper.implementations;

import net.chamosmp.chamoparty.paper.api.Reward;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChamoVote implements net.chamosmp.chamoparty.paper.api.Vote {

    private final String serviceName;
    private final long createdAt;
    private final Reward reward;
    private boolean rewardIsGiven;

    public ChamoVote(String serviceName, long createdAt, Reward reward, boolean rewardIsGiven) {
        this.serviceName = serviceName;
        this.createdAt = createdAt;
        this.reward = reward;
        this.rewardIsGiven = rewardIsGiven;
    }

    public ChamoVote(String serviceName, Reward reward, boolean rewardIsGiven) {
        this(serviceName, System.currentTimeMillis(), reward, rewardIsGiven);
    }


    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public long getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public Reward getReward() {
        return this.reward;
    }

    @Override
    public boolean rewardIsGive() {
        return this.rewardIsGiven;
    }

    @Override
    public void giveReward(Plugin plugin, Player player) {
        if (player == null || reward == null) return;
        try {
            this.rewardIsGiven = true;
            reward.give(plugin, player);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to give reward to " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
