package com.xephir64.switchboard.server.network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url = "jdbc:mariadb://localhost:3306/messenger";
    private final String user = "switchboard";
    private final String password = "*switchboard8@";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
