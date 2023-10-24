package databaseConnectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDbConnector implements IDatabaseConnector{

    private Connection connection = null;
    /**
     * @return SQLite connection
     */
    @Override
    public Connection connect(String connectionString) {
        Connection connection = getConnection();

        if(connection != null)
            return connection;

        try {
            // Load the SQLite Drive
            Class.forName("org.sqlite.JDBC");

            // create a connection to the database
            connection = DriverManager.getConnection(connectionString);

            System.out.println("Connection to SQLite has been established.");

        } catch (ClassNotFoundException |SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Connection getConnection() {
        return connection;
    }
}
