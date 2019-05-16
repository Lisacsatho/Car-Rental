package se.hkr.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DatabaseConnection implements Database {
    private static DatabaseConnection instance;

    private final String USERNAME = "rentall";
    private final String PASSWORD = "Xj0K9_Z_n331";
    private final String IP = "den1.mysql2.gear.host";
    private final String DATABASE = "RentAll";
    private final String ADDRESS = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&serverTimezone=UTC",
                                                IP, DATABASE, USERNAME, PASSWORD);
    private Queue<Connection> connections;
    private final int INITIAL_CONNECTIONS = 6;
    private final int MAX_CONNECTIONS = 10;
    private List<Connection> connectionsInUse;

    private DatabaseConnection() {
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionsInUse = new ArrayList<>();
    }

    @Override
    public void connect() throws Exception {
        connections = new LinkedList<>();
        for (int i = 0; i < INITIAL_CONNECTIONS; i++) {
            try {
                connections.offer(DriverManager.getConnection(ADDRESS));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void close() throws Exception {
        for (Connection connection : connections) {
            connection.close();
        }
        for (Connection connection : connectionsInUse) {
            connection.close();
        }
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(ADDRESS);
        } catch (SQLException e) {
            // TODO: Proper handling
            e.printStackTrace();
        }
        return null;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        if (connections.isEmpty()) {
            try {
                connection = DriverManager.getConnection(ADDRESS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            connection = connections.poll();
        }
        connectionsInUse.add(connection);
        System.out.println(connectionsInUse.size());
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connectionsInUse.size() + connections.size() <= MAX_CONNECTIONS) {
            connections.add(connection);
        }
        connectionsInUse.remove(connection);
        System.out.println(connectionsInUse.size());
    }
}
