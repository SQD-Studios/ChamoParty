package net.chamosmp.chamoparty.paper.storage.requets;

import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.api.Vote;
import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.save.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertRunnable implements Runnable {

    private final PlayerVote playerVote;
    private final Vote vote;
    private final Reward reward;
    private final IConnection iConnection;
    private int tryAmount = 0;

    /**
     *
     */
    public InsertRunnable(PlayerVote playerVote, Vote vote, Reward reward, IConnection iConnection) {
        super();
        this.playerVote = playerVote;
        this.vote = vote;
        this.reward = reward;
        this.iConnection = iConnection;
    }

    @Override
    public void run() {
        try {
            Connection connection = this.iConnection.getConnection();

            String request = "INSERT INTO chamoparty_votes (player_uuid, service_name, is_reward_give, reward_percent, commands, need_online, created_at) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(request);

            StringBuilder commands = new StringBuilder();
            for (int a = 0; a != this.reward.getCommands().size(); a++) {

                commands.append(this.reward.getCommands().get(a));
                if ((a + 1) < this.reward.getCommands().size()) {
                    commands.append(";");
                }

            }

            statement.setString(1, this.playerVote.getUniqueId().toString());
            statement.setString(2, this.vote.getServiceName());
            statement.setBoolean(3, this.vote.rewardIsGive());
            statement.setDouble(4, this.reward.getPercent());
            statement.setString(5, commands.toString());
            statement.setBoolean(6, this.reward.needToBeOnline());
            statement.setLong(7, this.vote.getCreatedAt());

            statement.executeUpdate();
            if (!connection.getAutoCommit()) {
                connection.commit();
            }

            statement.close();

        } catch (SQLException e) {
            this.tryAmount++;
            if (this.tryAmount < Config.maxSqlRetryAmoun) {
                try {
                    this.iConnection.disconnect();
                    this.iConnection.connect();
                    this.run();
                } catch (SQLException e1) {
                    Logger.log("Impossible to use MySQL storage!" + e1.getMessage(), Logger.LogType.ERROR);
                }
            } else {
                Logger.log("Could not connect to database." + e.getMessage(), Logger.LogType.ERROR);
            }
        }
    }

}
