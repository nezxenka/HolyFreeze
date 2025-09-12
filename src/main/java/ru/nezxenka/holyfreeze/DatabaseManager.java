package ru.nezxenka.holyfreeze;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final String serverName;
    private final String connectionUrl;
    private Connection connection;

    public DatabaseManager(String serverName, String host, String database, String username, String password) {
        this.serverName = serverName;
        this.connectionUrl = "jdbc:mysql://" + host + "/" + database + "?user=" + username + "&password=" + password + "&autoReconnect=true";
        initializeDatabase();
    }

    private void initializeDatabase() {
        CompletableFuture.runAsync(() -> {
            try {
                reconnectIfNeeded();
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(
                            "CREATE TABLE IF NOT EXISTS player_servers (" +
                                    "nickname VARCHAR(16) PRIMARY KEY, " +
                                    "server_name VARCHAR(64) NOT NULL)"
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void reconnectIfNeeded() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(1)) {
            connection = DriverManager.getConnection(connectionUrl);
        }
    }

    public CompletableFuture<Void> addPlayer(String nickname) {
        return CompletableFuture.runAsync(() -> {
            try {
                reconnectIfNeeded();
                String sql = "INSERT INTO player_servers (nickname, server_name) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE server_name = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, nickname);
                    statement.setString(2, serverName);
                    statement.setString(3, serverName);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<String> getServerByPlayer(String nickname) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                reconnectIfNeeded();
                String sql = "SELECT server_name FROM player_servers WHERE nickname = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, nickname);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            return resultSet.getString("server_name");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<Void> removePlayersByServer() {
        return CompletableFuture.runAsync(() -> {
            try {
                reconnectIfNeeded();
                String sql = "DELETE FROM player_servers WHERE server_name = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, serverName);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> removePlayer(String nickname) {
        return CompletableFuture.runAsync(() -> {
            try {
                reconnectIfNeeded();
                String sql = "DELETE FROM player_servers WHERE nickname = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, nickname);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}