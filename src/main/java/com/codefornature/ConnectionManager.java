package com.codefornature;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ConnectionManager {
    private static String url = "jdbc:mysql://127.0.0.1:3306/code_for_nature";
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String username = "root";
    private static String password = "5&2^*Zq7";
    private static Connection con;

    public static Connection getConnection(){
        try{
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, username, password);
            } catch(SQLException ex){
                ex.printStackTrace();
            }

        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return con;
    }

}
