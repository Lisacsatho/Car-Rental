package se.hkr.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements Database {
    private static DatabaseConnection instance;

    private Connection connection;
    private final String USERNAME = "rentall";
    private final String PASSWORD = "Xj0K9_Z_n331";
    private final String IP = "den1.mysql2.gear.host";
    private final String DATABASE = "RentAll";
    private final String ADDRESS = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&serverTimezone=UTC",
                                                IP, DATABASE, USERNAME, PASSWORD);

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
        connection = null;
    }
}
