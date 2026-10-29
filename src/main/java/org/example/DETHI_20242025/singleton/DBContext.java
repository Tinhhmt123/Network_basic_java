package org.example.DETHI_20242025.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {

    private static DBContext instance;
    private Connection connection;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        String path = "D:\\My_Study_of_TINH\\LTM\\Test\\DBBank.accdb";
        String URL = "jdbc:ucanaccess://"+path;
        connection = DriverManager.getConnection(URL);
        return connection;
    }

    public static DBContext getInstance() {
        if (instance == null) {
            instance = new DBContext();
        }
        return instance;
    }
}
