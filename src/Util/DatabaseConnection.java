
package Util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL, USER, PASSWORD;
   
    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("Config/db.properties")) {
            
            Properties prop = new Properties();
            prop.load(input);
            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {        
            return DriverManager.getConnection(URL, USER, PASSWORD);    
    }
}
