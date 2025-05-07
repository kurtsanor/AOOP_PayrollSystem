
package oopClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
     
    private static String URL = "jdbc:MYSQL://localhost:3306/mtrphdb";
    private static String USER = "root";
    private static String PASSWORD = "kurt01021974";
    
    public static Connection Connect(){     
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
