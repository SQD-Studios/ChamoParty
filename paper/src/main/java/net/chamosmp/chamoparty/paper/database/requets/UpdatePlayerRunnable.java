package net.chamosmp.chamoparty.paper.database.requets;

import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import net.chamosmp.sqdlib.util.LogType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class UpdatePlayerRunnable implements Runnable {

    private final IConnection iConnection;
    private final UUID uniqueId;
    private int tryAmount = 0;

    /**
     * @param connection
     * @param playerVote
     */
    public UpdatePlayerRunnable(IConnection connection, UUID uniqueId) {
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
            if (this.tryAmount < LegacyJsonConfig.maxSqlRetryAmoun) {
                try {
                    this.iConnection.disconnect();
                    this.iConnection.connect();
                    this.run();
                } catch (SQLException e1) {
                    LoggerUtil.log(LogType.SEVERE, "Impossible to use MySQL storage!");
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

}
