package databaseConnectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDbConnector implements IDatabaseConnector {

    // The single instance of the class
    private static SQLiteDbConnector instance;

    // Connection object
    private Connection connection;

    // Private constructor so no other class can instantiate it
    private SQLiteDbConnector() {
    }

    // Public static method to get the single instance
    public static synchronized SQLiteDbConnector getInstance() {
        if (instance == null) {
            instance = new SQLiteDbConnector();
        }
        return instance;
    }

    /**
     * Connects to the SQLite database or returns the existing connection
     * @param connectionString Connection string for the SQLite database
     * @return Connection to the database
     */
    @Override
    public Connection connect(String connectionString) {
        if (connection != null) {
            return connection;
        }

        try {
            // Load the SQLite Driver
            Class.forName("org.sqlite.JDBC");

            // Create a connection to the database
            connection = DriverManager.getConnection(connectionString);

//            System.out.println("Connection to SQLite has been established.");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    /**
     * Disconnects from the SQLite database
     */
    @Override
    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                connection = null; // Ensure connection is set to null after closing
            }
        } catch (SQLException e) {
            System.out.println("Disconnected from SQLite.");
            e.printStackTrace();
        }
    }

    /**
     * Gets the current database connection
     * @return The current database connection
     */
    public Connection getConnection() {
        return connection;
    }
}

