//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sample.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    // It's important that the user makes a config.settings file from the example in the config folder.
    // With the information filled.
    private static final String PROP_FILE = "config/config.settings";
    private SQLServerDataSource dataSource;

    /**
     * Sets sensitive login information for the database connection.
     *
     * @throws SQLException If the DataSource cannot be made.
     * @throws IOException If the file path cannot be read.
     */
    public DatabaseConnector() throws SQLException, IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(PROP_FILE));
        this.dataSource = new SQLServerDataSource();
        this.dataSource.setServerName(databaseProperties.getProperty("Server"));
        this.dataSource.setDatabaseName(databaseProperties.getProperty("Database"));
        this.dataSource.setUser(databaseProperties.getProperty("User"));
        this.dataSource.setPassword(databaseProperties.getProperty("Password"));
        this.dataSource.setPortNumber(Integer.parseInt(databaseProperties.getProperty("Port")));
        this.dataSource.setTrustServerCertificate(true);
    }

    /**
     * Gets a Connection object with a connection to the database.
     *
     * @return A Connection object with the connection to the database.
     * @throws SQLServerException If the connection cannot be made.
     */
    public Connection getConnection() throws SQLServerException {
        return this.dataSource.getConnection();
    }
}
