package pl.umcs.oop.words.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private Connection connection;

    public void connect(String path) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
//            System.out.println("Connected");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            connection.close();
//            System.out.println("Disconnected");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

