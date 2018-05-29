package com.leisen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    //JDBC config
    private static String driver = "com.mysql.cj.jdbc.Driver"; //新版本的driver
    private static String url = "jdbc:mysql://localhost:3306/nbstorage?useSSL=false";
    private static String user = "leisen";
    private static String password = "bjleisen2014";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
