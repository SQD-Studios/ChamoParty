package net.chamosmp.chamoparty.paper.votestorage.requets;

import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.utils.Utils;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectVoteCountRunnable extends Utils implements Runnable {

    private final IConnection iConnection;
    private final IStorage iStorage;
    private int tryAmount = 0;

    /**
     * @param iConnection
     * @param iStorage
     */
    public SelectVoteCountRunnable(IConnection iConnection, IStorage iStorage) {
        super();
        this.iConnection = iConnection;
        this.iStorage = iStorage;
    }

    @Override
    public void run() {
        try {
            Connection connection = this.iConnection.getConnection();

            String request = "SELECT * FROM chamoparty_count";
            PreparedStatement statement = connection.prepareStatement(request);
            ResultSet resultSet = statement.executeQuery();

            if (!connection.getAutoCommit()) {
                connection.commit();
            }

            if (resultSet.next()) {
                this.iStorage.setVoteCount(resultSet.getLong("vote"));
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
                    this.iStorage.setVoteCount(0);
                    Logger.log("Impossible to use MySQL storage!", LogType.ERROR);
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

}
