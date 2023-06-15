package pl.umcs.oop.words.client;

import pl.umcs.oop.words.CurrentTime;
import pl.umcs.oop.words.HelloController;
import pl.umcs.oop.words.database.DatabaseConnection;

import java.io.*;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServerThread extends Thread {
    private final Socket socket;
    private final HelloController helloController;
    private final DatabaseConnection connection;
    private final String tableName;
    private PrintWriter writer;

    public ServerThread(String address, int port, HelloController helloController) {
        this.helloController = helloController;
        connection = new DatabaseConnection();
        connection.connect("/home/ppirog/IdeaProjects/words/src/main/java/pl/umcs/oop/words/database/words.db");
        int tablesCount;
        try {
            PreparedStatement countTables = connection.getConnection()
                    .prepareStatement("SELECT COUNT(*) FROM sqlite_master WHERE type='table';");
            countTables.execute();
            tablesCount = countTables.getResultSet().getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tableName = "Table" + tablesCount;

        try {
            PreparedStatement createTableStatement = connection.getConnection()
                    .prepareStatement(String.format("CREATE TABLE %s (time TEXT, word TEXT)", tableName));
            createTableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            this.socket = new Socket(address, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
            String message;
            while ((message = reader.readLine()) != null) {
                addToGuiList(message);
//                System.out.println("Client: " + message);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addToGuiList(String message) {
        CurrentTime currentTime = new CurrentTime();
        if (helloController.getFilterField().getText().length() > 0) {
//            System.out.println("widze żę filtrujesz");
            helloController.setFilterField(message, currentTime);
        } else {
//            System.out.println("przestałęś filtrować");
            helloController.addToList(message, currentTime);
        }
        addToDatabase(currentTime.getTime(), message);
    }


    void addToDatabase(String time, String message) {
        try {

            PreparedStatement statement = connection.getConnection()
                    .prepareStatement(String.format("INSERT INTO %s (time, word) VALUES (?, ?)", tableName));
            statement.setString(1, time);
            statement.setString(2, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        writer.println(message);
    }

}
