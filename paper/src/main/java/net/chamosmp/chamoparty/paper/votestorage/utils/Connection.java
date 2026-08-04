package net.chamosmp.chamoparty.paper.votestorage.utils;

import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.api.Vote;
import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.votestorage.requets.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class Connection implements IConnection {

    private java.sql.Connection connection;
    private final Storage storage;
    private final String user;
    private final String password;
    private final String host;
    private final String dataBase;
    private final int port;

    /**
     * @param connection
     * @param storage
     * @param user
     * @param password
     * @param host
     * @param dataBase
     */
    public Connection(Storage storage, String user, String password, String host, String dataBase, int port) {
        super();
        this.storage = storage;
        this.user = user;
        this.password = password;
        this.host = host;
        this.dataBase = dataBase;
        this.port = port;
    }

    @Override
    public java.sql.Connection getConnection() {
        return connection;
    }

    @Override
    public void connect() throws SQLException {
        String url = this.storage.getUrlBase() + this.host + ":" + this.port + "/" + this.dataBase;
        this.connection = DriverManager.getConnection(url, this.user, this.password);
    }

    @Override
    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                Logger.log("Connection close failed" + e);
            }
        }
    }

    @Override
    public void getAndRefreshConnection(Runnable runnable) {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                Thread thread = new Thread(() -> {
                    try {
                        connect();
                        runnable.run();
                    } catch (SQLException e) {
                        Logger.log(e.getMessage(), Logger.LogType.ERROR);
                    }
                });
                thread.start();
            } else {
                runnable.run();
            }
        } catch (SQLException e) {
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
        }
    }

    @Override
    public void updateVoteCount(long amount) {
        this.getAndRefreshConnection(() -> {
            Thread thread = new Thread(new UpdateCountRunnable(this, amount));
            thread.start();
        });
    }

    @Override
    public void asyncFetchPlayer(UUID uuid, Consumer<Optional<PlayerVote>> consumer, IStorage iStorage) {
        this.getAndRefreshConnection(() -> {
            Thread thread = new Thread(new SelectVotesRunnable(this, uuid, consumer, iStorage));
            thread.start();
        });
    }

    @Override
    public void asyncInsert(PlayerVote playerVote, Vote vote, Reward reward) {
        this.getAndRefreshConnection(() -> {
            Thread thread = new Thread(new InsertRunnable(playerVote, vote, reward, this));
            thread.start();
        });
    }

    @Override
    public void fetchVotes(IStorage sqlStorage) {
        this.getAndRefreshConnection(() -> {
            Runnable runnable = new SelectVoteCountRunnable(this, sqlStorage);
            runnable.run();
        });
    }

    @Override
    public void updateRewards(UUID uniqueId) {
        this.getAndRefreshConnection(() -> {
            Thread thread = new Thread(new UpdatePlayerRunnable(this, uniqueId));
            thread.start();
        });
    }

}