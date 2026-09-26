package net.chamosmp.chamoparty.paper.database.requets;

import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import net.chamosmp.sqdlib.util.LogType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectVoteCountRunnable implements Runnable {

    private final IConnection iConnection;
    private final IStorage iStorage;
    private int tryAmount = 0;

    /**
     * @param iConnection
     * @param iStorage
     */
    public SelectVoteCountRunnable(IConnection iConnection, IStorage iStorage) {

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
                    LoggerUtil.log(LogType.SEVERE, "Impossible to use MySQL storage!");
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

}
