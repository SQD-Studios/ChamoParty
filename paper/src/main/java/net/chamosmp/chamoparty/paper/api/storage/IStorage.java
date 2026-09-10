package net.chamosmp.chamoparty.paper.api.storage;

import net.chamosmp.chamoparty.paper.api.PlayerManager;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.api.Vote;
import net.chamosmp.chamoparty.paper.core.utils.storage.Saveable;

import java.util.Map;
import java.util.UUID;

public interface IStorage extends PlayerManager, Saveable {

    /**
     *
     * @return
     */
    long getVoteCount();

    /**
     *
     * @param amount
     */
    void addVoteCount(long amount);

    /**
     *
     * @param amount
     */
    void setVoteCount(long amount);

    /**
     * Add vote to datebase
     *
     * @param playerVote
     * @param vote
     * @param reward
     */
    void insertVote(PlayerVote playerVote, Vote vote, Reward reward);

    /**
     * Start vote party
     */
    void startVoteParty();

    /**
     * Update reward in datebase
     *
     * @param uniqueId
     */
    void updateRewards(UUID uniqueId);

    /**
     *
     * @param playerVote
     */
    void createPlayer(PlayerVote playerVote);

}