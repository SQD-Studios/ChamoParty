package net.chamosmp.chamoparty.paper.votestorage.requets;

import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import net.chamosmp.chamoparty.paper.save.JsonConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class UpdatePlayerRunnable extends Utils implements Runnable {

    private final IConnection iConnection;
    private final UUID uniqueId;
    private int tryAmount = 0;

    /**
     * @param connection
     * @param playerVote
     */
    public UpdatePlayerRunnable(IConnection connection, UUID uniqueId) {
        super();
        this.iConnection = connection;
        this.uniqueId = uniqueId;
    }

    @Override
    public void run() {
        try {
            Connection connection = this.iConnection.getConnection();

            if (connection == null || connection.isClosed()) {
                this.iConnection.connect();
                connection = this.iConnection.getConnection();
            }

            String request = "UPDATE chamoparty_votes SET is_reward_give = 1 WHERE player_uuid = ?;";
            PreparedStatement statement = connection.prepareStatement(request);

            statement.setString(1, this.uniqueId.toString());

            statement.executeUpdate();
            if (!connection.getAutoCommit()) {
                connection.commit();
            }

            statement.close();

        } catch (SQLException e) {
            this.tryAmount++;
            if (this.tryAmount < JsonConfig.maxSqlRetryAmoun) {
                try {
                    this.iConnection.disconnect();
                    this.iConnection.connect();
                    this.run();
                } catch (SQLException e1) {
                    Logger.log("Impossible to use MySQL storage!", LogType.ERROR);
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

}
