package com.github.sandokandias.payments;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DddDataSource {

    private static HikariConfig config = new HikariConfig();

    private DddDataSource() {}

    public static HikariDataSource getDataSource() {
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/ddd_springboot");
        config.setUsername("ddd_springboot");
        config.setPassword("ddd_springboot");
        config.addDataSourceProperty("cachePrepStmts" , "true");
        config.addDataSourceProperty("prepStmtCacheSize" , "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
        return new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
