package org.simpmc.simppay.database;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.simpmc.simppay.api.DatabaseSettings;

import java.sql.SQLException;

public class Database {

    private final HikariDataSource dataSource;
    private final DataSourceConnectionSource connectionSource;

    public Database(DatabaseSettings db) throws SQLException {
        // Retrieve config values from your ConfigManager
        // 'database' is used here to determine the type ("mysql" or "sqlite")
        String dbType = db.getType();
        String host = db.getHost();
        int port = db.getPort();
        String username = db.getUsername();
        String password = db.getPassword();

        // HikariCP configuration
        HikariConfig config = new HikariConfig();
        String jdbcUrl;
        String driverClassName;

        if (dbType.equalsIgnoreCase("mysql")) {
            // For MySQL, you need a database name.
            // Ensure you have added a field in your config for the MySQL database name.
            String dbName = db.getDatabase(); // e.g., "mydatabase"
            jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
            driverClassName = "com.mysql.cj.jdbc.Driver";

            // Additional MySQL-specific HikariCP settings
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        } else if (dbType.equalsIgnoreCase("h2")) {
            jdbcUrl = "jdbc:h2:./" + host + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
            driverClassName = "org.h2.Driver";
        } else {
            throw new RuntimeException("Unsupported database type: " + dbType);
        }

        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName(driverClassName);
        config.setUsername(username);
        config.setPassword(password);
        // General HikariCP settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000); // 30 seconds
        config.setIdleTimeout(600000);        // 10 minutes
        config.setMaxLifetime(1800000);       // 30 minutes

        dataSource = new HikariDataSource(config);
        connectionSource = new DataSourceConnectionSource(dataSource, jdbcUrl);

//         Create tables if they do not exist
    }

    /**
     * Closes both the ORMLite connection source and the HikariCP data source.
     */
    public void close() {
        if (connectionSource != null) {
            connectionSource.closeQuietly();
        }
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
