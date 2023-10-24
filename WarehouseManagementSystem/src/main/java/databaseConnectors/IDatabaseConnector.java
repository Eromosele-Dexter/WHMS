package databaseConnectors;

import java.sql.Connection;

public interface IDatabaseConnector {

    public Connection connect(String connectionString);
    public void disconnect();
}
