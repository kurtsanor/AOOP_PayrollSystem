
package Model;

import DatabaseConnection.DatabaseConnection;
import Domains.LeaveRequest;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LeaveDAO {   
    
    public LeaveDAO() {}
       
    public List <LeaveRequest> getAllRecords () throws SQLException {
        List <LeaveRequest> leaveRecords = new ArrayList<>();
        String query = "SELECT l.leaveID, l.employeeID, lt.leaveTypeName, l.startDate, l.endDate, l.status, l.submittedDate, l.processedDate, l.remarks" +
"                FROM leaves l JOIN leaveType lt ON l.leaveTypeID = lt.leaveTypeID ORDER BY l.leaveID DESC";
        
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();                                     
            while (rs.next()) {
                LeaveRequest leaveRequest = createLeaveFromResultSet(rs);
                leaveRecords.add(leaveRequest);
            }                                 
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve all leave records", e);
        }
        
        return leaveRecords;
    }
          
    public List <LeaveRequest> getLeavesByEmployeeID (int employeeID) throws SQLException {
        List <LeaveRequest> leaveRecords = new ArrayList<>();
        String query = "SELECT l.leaveID, l.employeeID, lt.leaveTypeName, l.startDate, l.endDate, l.status, l.submittedDate, l.processedDate, l.remarks" +
"                FROM leaves l JOIN leaveType lt ON l.leaveTypeID = lt.leaveTypeID WHERE l.employeeID = ? ORDER BY l.leaveID DESC";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {            
            pst.setInt(1, employeeID);
            ResultSet rs = pst.executeQuery();                                    
            while (rs.next()) {
                LeaveRequest leaveRequest = createLeaveFromResultSet(rs);
                leaveRecords.add(leaveRequest);
             }                                 
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve leave records by employee id", e);
        }
        
        return leaveRecords;
    }
    
    public LeaveRequest getLeaveByLeaveID (int leaveID) throws SQLException {
        String query = "SELECT * FROM leaves WHERE leaveID = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {            
            pst.setInt(1, leaveID);
            ResultSet rs = pst.executeQuery();                                    
            if (rs.next()) {
                return createLeaveFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve leave record by leave id", e);
        }
        return null;
    }
        
   
    public boolean submitLeaveRequest (LeaveRequest leaveRequest) throws SQLException {
        int leaveTypeID = -1;
        
        String leaveTypeQuery = "SELECT leaveTypeID FROM leavetype WHERE leaveTypeName = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement leaveTypePst = connection.prepareStatement(leaveTypeQuery)) {
            leaveTypePst.setString(1, leaveRequest.getLeaveType());
            ResultSet rs = leaveTypePst.executeQuery();
            if (rs.next()) {
                leaveTypeID = rs.getInt("leaveTypeID");
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to get leave type id", e);
        }
        
        String insertQuery = "INSERT INTO leaves "
                     + "(employeeID, leaveTypeID, startDate, endDate, status, submittedDate, remarks) VALUES (?,?,?,?,?,?,?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(insertQuery)) {
            pst.setInt(1, leaveRequest.getEmployeeID());
            pst.setInt(2, leaveTypeID);
            pst.setDate(3, Date.valueOf(leaveRequest.getStartDate()));
            pst.setDate(4, Date.valueOf(leaveRequest.getEndDate()));
            pst.setString(5, leaveRequest.getStatus());
            pst.setTimestamp(6, Timestamp.valueOf(leaveRequest.getSubmittedDate()));
            pst.setString(7, leaveRequest.getRemarks());
            
            return pst.executeUpdate() > 0;
                                                                            
        } catch (SQLException e) {
            throw new SQLException("Failed to submit leave request", e);
        }     
    }
        
    public boolean updateStatus (int leaveID, String newStatus, LocalDateTime dateProcesed) throws SQLException {
        String query = "UPDATE leaves SET status = ?, processedDate = ? WHERE leaveID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {             
            pst.setString(1, newStatus);
            pst.setTimestamp(2, Timestamp.valueOf(dateProcesed));
            pst.setInt(3, leaveID);
            return pst.executeUpdate() > 0;
                                                                 
        } catch (SQLException e) {
            throw new SQLException("Failed to update status", e);
        }
    }
       
    private LeaveRequest createLeaveFromResultSet (ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("processedDate");
        LocalDateTime processedDate = (timestamp != null) ? timestamp.toLocalDateTime(): null;
        LeaveRequest leaveRequest = new LeaveRequest(
            rs.getInt("employeeID"),
            rs.getString("leaveTypeName"),
            rs.getDate("startDate").toLocalDate(),
            rs.getDate("endDate").toLocalDate(),
            rs.getString("status"),
            rs.getTimestamp("submittedDate").toLocalDateTime(),
            rs.getString("remarks")
        );
        leaveRequest.setLeaveID(rs.getInt("leaveID"));
        leaveRequest.setProcessedDate(processedDate);
        return leaveRequest;
    }
  
}
