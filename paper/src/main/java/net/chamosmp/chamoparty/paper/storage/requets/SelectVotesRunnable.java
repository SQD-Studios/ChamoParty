package net.chamosmp.chamoparty.paper.storage.requets;

import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import net.chamosmp.chamoparty.paper.implementations.ChamoPlayerVote;
import net.chamosmp.chamoparty.paper.implementations.ChamoReward;
import net.chamosmp.chamoparty.paper.implementations.ChamoVote;
import net.chamosmp.chamoparty.paper.save.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class SelectVotesRunnable extends Utils implements Runnable {

    private final IConnection iConnection;
    private final UUID uniqueId;
    private final Consumer<Optional<net.chamosmp.chamoparty.paper.api.PlayerVote>> consumer;
    private final IStorage iStorage;
    private int tryAmount = 0;

    /**
     * @param storage
     * @param iConnection
     * @param uniqueId
     * @param consumer
     * @param iStorage
     */
    public SelectVotesRunnable(IConnection iConnection, UUID uniqueId, Consumer<Optional<net.chamosmp.chamoparty.paper.api.PlayerVote>> consumer,
                               IStorage iStorage) {
        super();
        this.iConnection = iConnection;
        this.uniqueId = uniqueId;
        this.consumer = consumer;
        this.iStorage = iStorage;
    }

    @Override
    public void run() {
        try {
            Connection connection = this.iConnection.getConnection();

            List<net.chamosmp.chamoparty.paper.api.Vote> votes = new ArrayList<>();

            String request = "SELECT * FROM chamoparty_votes WHERE player_uuid = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, this.uniqueId.toString());
            ResultSet resultSet = statement.executeQuery();

            if (!connection.getAutoCommit()) {
                connection.commit();
            }

            while (resultSet.next()) {

                String serviceName = resultSet.getString("service_name");
                boolean isRewardGive = resultSet.getBoolean("is_reward_give");
                double rewardPercent = resultSet.getDouble("reward_percent");
                String commandsAsString = resultSet.getString("commands");
                boolean needOnline = resultSet.getBoolean("need_online");
                long createdAt = resultSet.getLong("created_at");

                List<String> commands = Arrays.asList(commandsAsString.split(";"));
                ChamoReward chamoReward = new ChamoReward(rewardPercent, commands, needOnline, new ArrayList<>());
                net.chamosmp.chamoparty.paper.api.Vote vote = new ChamoVote(serviceName, createdAt, chamoReward, isRewardGive);
                votes.add(vote);

            }

            statement.close();
            ChamoPlayerVote chamoPlayerVote = new ChamoPlayerVote(this.uniqueId, votes);
            this.iStorage.createPlayer(chamoPlayerVote);
            this.consumer.accept(Optional.of(chamoPlayerVote));

        } catch (SQLException e) {
            this.tryAmount++;
            if (this.tryAmount < Config.maxSqlRetryAmoun) {
                try {
                    this.iConnection.disconnect();
                    this.iConnection.connect();
                    this.run();
                } catch (SQLException e1) {
                    this.consumer.accept(Optional.empty());
                    Logger.log("Impossible to use MySQL storage!", LogType.ERROR);
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

}
