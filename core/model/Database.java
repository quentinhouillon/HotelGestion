package core.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:model.db";
    private Connection connection;

    public Database() {
        File dbFile = new File("./model.db");
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                System.err.println("Failed to create database file: " + e.getMessage());
            }
        }
        try {
            try {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection(DB_URL);
            } catch (ClassNotFoundException e) {
                System.err.println("SQLite JDBC driver not found: " + e.getMessage());
            }
            initBase();
        } catch (SQLException e) {
            System.err.println("Failed to connect to SQLite database: " + e.getMessage());
        }
    }

    public void initBase() {
        String createClientTable = "CREATE TABLE IF NOT EXISTS Client (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "lastName TEXT NOT NULL," +
                "firstName TEXT NOT NULL," +
                "phone TEXT NOT NULL" +
                ");";

        String createReservationTable = "CREATE TABLE IF NOT EXISTS Reservation (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "client_id INTEGER NOT NULL," +
                "room_id INTEGER NOT NULL," +
                "start_date TEXT NOT NULL," +
                "end_date TEXT NOT NULL," +
                "FOREIGN KEY(client_id) REFERENCES Client(id)," +
                "FOREIGN KEY(room_id) REFERENCES Room(id)" +
                ");";

        String createStayTable = "CREATE TABLE IF NOT EXISTS Stay (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "reservation_id INTEGER NOT NULL," +
                "payment STRING," +
                "FOREIGN KEY(reservation_id) REFERENCES Reservation(id)" +
                ");";

        String CreateConsommationTable = "CREATE TABLE IF NOT EXISTS Consommation (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "stay_id INTEGER NOT NULL," +
                "conso TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY(stay_id) REFERENCES Stay(id)" +
                ");";

        try {
            this.connection.createStatement().executeUpdate(createClientTable);
            this.connection.createStatement().executeUpdate(createReservationTable);
            this.connection.createStatement().executeUpdate(createStayTable);
            this.connection.createStatement().executeUpdate(CreateConsommationTable);
            this.connection.close();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database tables: " + e.getMessage());
        }
    }

    public boolean executeUpdateQuery(String sqlQuery, Object[] params) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to execute update query: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> executeReadQuery(String sqlQuery) {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {

            try (ResultSet rs = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }

        } catch (SQLException e) {
            System.err.println("Failed to execute read query: " + e.getMessage());
        }
        return results;
    }
}
