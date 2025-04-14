package oopClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserAuthentication  {
    
    private final Connection connection;
    
    public UserAuthentication (Connection connection) {
        this.connection = connection;
    }
                   
    public int getAuthenticatedID (String username, String password) {
        String query = "SELECT employeeID FROM credentials WHERE BINARY username = ? AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                int employeeID = rs.getInt("employeeID");
                return employeeID;
            }                     
        } catch (SQLException e) {
            throw new RuntimeException("Failed to authenticate user",e);
        }
        return -1; // return -1 if the credentials is incorrect
    }         
}
