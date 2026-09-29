package net.chamosmp.chamoparty.paper.database.utils;

import net.chamosmp.chamoparty.api.storage.Storage;
import net.chamosmp.chamoparty.paper.api.PlayerVote;
import net.chamosmp.chamoparty.paper.api.Reward;
import net.chamosmp.chamoparty.paper.api.Vote;
import net.chamosmp.chamoparty.paper.api.storage.IConnection;
import net.chamosmp.chamoparty.paper.api.storage.IStorage;
import net.chamosmp.chamoparty.paper.database.requets.*;
import net.chamosmp.sqdlib.paper.util.LoggerUtil;
import net.chamosmp.sqdlib.util.log.LogType;
import org.bukkit.plugin.Plugin;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class SqlConnection implements IConnection {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SqlConnection.class);
    private final Storage storage;
    private final Plugin plugin;
    private Connection connection;

    public SqlConnection(Storage storage, Plugin plugin) {
        this.storage = storage;
        this.plugin = plugin;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void connect() throws SQLException {
        switch (this.storage) {
            case MYSQL, MARIADB -> {
                String user = plugin.getConfig().getString("database.sql.sql-credentials.user");
                String password = plugin.getConfig().getString("database.sql.sql-credentials.password");
                String host = plugin.getConfig().getString("database.sql.sql-credentials.host");
                String database = plugin.getConfig().getString("database.sql.sql-credentials.database");
                int port = plugin.getConfig().getInt("database.sql.sql-credentials.port");

                String url = this.storage.getUrlBase() + host + ":" + port + "/" + database;
                this.connection = DriverManager.getConnection(url, user, password);
            }
            default -> {
                String url = this.storage.getUrlBase() + plugin.getDataPath() + "/sqlite.db";
                this.connection = DriverManager.getConnection(url);
            }
        }
    }

    @Override
    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                LoggerUtil.log(LogType.SEVERE, "Connection close failed" + e);
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
                        log.error("Failed to get and refresh the connection: ", e);
                    }
                });
                thread.start();
            } else {
                runnable.run();
            }
        } catch (SQLException e) {
            LoggerUtil.log(LogType.SEVERE, e.getMessage());
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