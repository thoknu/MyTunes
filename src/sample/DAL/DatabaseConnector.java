//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sample.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class DatabaseConnector {
    private static final String PROP_FILE = "mytunes/config/config.settings";
    private SQLServerDataSource dataSource;

    public DatabaseConnector() throws IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(new File("mytunes/config/config.settings")));
        this.dataSource = new SQLServerDataSource();
        this.dataSource.setServerName(databaseProperties.getProperty("Server"));
        this.dataSource.setDatabaseName(databaseProperties.getProperty("Database"));
        this.dataSource.setUser(databaseProperties.getProperty("User"));
        this.dataSource.setPassword(databaseProperties.getProperty("Password"));
        this.dataSource.setPortNumber(1433);
        this.dataSource.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException {
        return this.dataSource.getConnection();
    }
}
