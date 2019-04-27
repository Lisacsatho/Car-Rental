package se.hkr.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements Database {
    private static DatabaseConnection instance;

    private Connection connection;
    private final String USERNAME = "root";
    private final String PASSWORD = "password";
    private final String IP = "127.0.0.1";
    private final String DATABASE = "CarRental";
    private final String ADDRESS = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&serverTimezone=UTC",
                                                IP, DATABASE, USERNAME, PASSWORD);

    private DatabaseConnection() {

    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    @Override
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(ADDRESS);
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
