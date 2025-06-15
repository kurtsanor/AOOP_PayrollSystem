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
    
    public boolean isEmployeePasswordCorrect(int employeeID, String password) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("CALL credentialsCheckEmployeePassword(?,?)")) {
            stmt.setInt(1, employeeID);
            stmt.setString(2, password);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new SQLException("Failed to check employee password", e);
        }
    }
    
    public boolean updatePasswordByEmployeeID(int employeeID, String newPassword) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("CALL credentialsUpdatePassword(?,?)")){
            stmt.setInt(1, employeeID);
            stmt.setString(2, newPassword);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLException("Failed to update password", e);
        }
    }
    
    public boolean updateTotpSecret(int employeeID, String secret) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("CALL credentialsUpdateTotpSecret(?,?)")) {
            stmt.setInt(1, employeeID);
            stmt.setString(2, secret);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Failed to insert totp secret", e);
        }
    }
    
    public String getTotpSecretByEmployeeID(int employeeID) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("CALL credentialsGetTotpSecret(?)")) {
            stmt.setInt(1, employeeID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next()? rs.getString("totpSecret"): null;
            }
            
        } catch (SQLException e) {
            throw new SQLException("Failed to get TOTP secret", e);
        }
    }
    
    public boolean has2FAEnabled(int employeeID) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("CALL credentialsHas2FAEnabled(?)")){
            stmt.setInt(1, employeeID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException e) {
            throw new SQLException("Failed to check has 2FA enabled", e);
        }
    }
}
