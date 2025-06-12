package Model;
import Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;


public class CredentialsDAO  {    
    
    public CredentialsDAO () {}
                   
    public int getAuthenticatedID (String username, String password) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL credentialsGetUserID(?,?)}")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next()? rs.getInt("employeeID") : -1;
            }
                                                      
        } catch (SQLException e) {
            throw new SQLException("Failed to authenticate user",e);
        }
    }         
}
