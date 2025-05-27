package com.ic.group3.group3projectphase4nk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    //database connection details
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/group3project?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "rootrootroot123$"; // Update this to whatever password you set
    
    //method to get database connection
    public static Connection getConnection() throws SQLException {
        try {
            //register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            //create connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
    
    //test the connection
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            System.out.println("Database connection successful!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}