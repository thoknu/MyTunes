package sample.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class DatabaseConnector {
    private static final String PROP_FILE = "config/config.settings";
    private SQLServerDataSource dataSource;
}
