/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author knim1
 */
public class Db {
    // For class demo: hard-coded (move to properties/env for production)
    private static final String URL = "jdbc:mysql://localhost:3306/dealership_inventory?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Ebmexogd12@";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // ensure driver loaded
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
