package edu.eskisehir.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection connection = null;

    public static Connection connect() {

        Properties connectionProperties = new Properties();
        connectionProperties.put("url", "jdbc:mysql://localhost:3306/barbershop");
        connectionProperties.put("username", "root");
        connectionProperties.put("password", "");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionProperties.getProperty("url"), connectionProperties.getProperty("username"),
                    connectionProperties.getProperty("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

//        System.out.println("Connected to database");

        return connection;
    }

}
