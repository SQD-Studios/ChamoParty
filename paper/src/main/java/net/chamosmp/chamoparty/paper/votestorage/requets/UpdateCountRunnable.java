package net.chamosmp.chamoparty.paper.votestorage.requets;

import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.save.JsonConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateCountRunnable implements Runnable {

    private final IConnection iConnection;
    private final long value;
    private int tryAmount = 0;

    /**
     * @param connection
     * @param value
     */
    public UpdateCountRunnable(IConnection connection, long value) {
        super();
        this.iConnection = connection;
        this.value = value;
    }

    @Override
    public void run() {
        try {
            Connection connection = this.iConnection.getConnection();

            if (connection == null || connection.isClosed()) {
                this.iConnection.connect();
                connection = this.iConnection.getConnection();
            }

            String selectRequest = "select count(*) as somme from chamoparty_count";
            PreparedStatement statementSelect = connection.prepareStatement(selectRequest);
            ResultSet resultSetSelect = statementSelect.executeQuery();

            if (!connection.getAutoCommit()) {
                connection.commit();
            }

            resultSetSelect.next();
            int value = resultSetSelect.getInt("somme");

            statementSelect.close();

            if (value < 1) {

                String insertRequest = "insert into chamoparty_count (vote) values (0);";
                PreparedStatement statement = connection.prepareStatement(insertRequest);
                statement.executeUpdate();
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
                statement.close();

            }

            String request = "UPDATE chamoparty_count SET vote = ? where true";
            PreparedStatement statement = connection.prepareStatement(request);

            statement.setLong(1, this.value);

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
