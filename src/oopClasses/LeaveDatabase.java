
package oopClasses;

import Domains.LeaveRequest;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveDatabase {
   
    private final Connection connection;
    
    public LeaveDatabase(Connection connection) {
        this.connection = connection;
    }
       
    public List <LeaveRequest> getAllRecords () {
        List <LeaveRequest> leaveRecords = new ArrayList<>();
        String query = "SELECT * FROM leaves";
        
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();                                     
            while (rs.next()) {
                LeaveRequest leaveRequest = createLeaveFromResultSet(rs);
                leaveRecords.add(leaveRequest);
            }                                 
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all leave records", e);
        }
        
        return leaveRecords;
    }
          
    public List <LeaveRequest> getLeavesByEmployeeID (int employeeID) {
        List <LeaveRequest> leaveRecords = new ArrayList<>();
        String query = "SELECT * FROM leaves WHERE employeeID = ?";
        
        try (PreparedStatement pst = connection.prepareStatement(query)) {            
            pst.setInt(1, employeeID);
            ResultSet rs = pst.executeQuery();                                    
            while (rs.next()) {
                LeaveRequest leaveRequest = createLeaveFromResultSet(rs);
                leaveRecords.add(leaveRequest);
             }                                 
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve leave records by employee id", e);
        }
        
        return leaveRecords;
    }
    
    public LeaveRequest getLeaveByLeaveID (int leaveID) {
        String query = "SELECT * FROM leaves WHERE leaveID = ?";
        
        try (PreparedStatement pst = connection.prepareStatement(query)) {            
            pst.setInt(1, leaveID);
            ResultSet rs = pst.executeQuery();                                    
            if (rs.next()) {
                return createLeaveFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve leave record by leave id", e);
        }
        return null;
    }
        
   
    public boolean submitLeaveRequest (LeaveRequest leaveRequest) {       
        String query = "INSERT INTO leaves "
                     + "(employeeID, leaveType, startDate, endDate, status, submittedDate, remarks) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, leaveRequest.getEmployeeID());
            pst.setString(2, leaveRequest.getLeaveType());
            pst.setDate(3, Date.valueOf(leaveRequest.getStartDate()));
            pst.setDate(4, Date.valueOf(leaveRequest.getEndDate()));
            pst.setString(5, leaveRequest.getStatus());
            pst.setTimestamp(6, Timestamp.valueOf(leaveRequest.getSubmittedDate()));
            pst.setString(7, leaveRequest.getRemarks());
            
            return pst.executeUpdate() > 0;
                                                                            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to submit leave request", e);
        }     
    }
        
    public boolean updateStatus (int leaveID, String newStatus, LocalDateTime dateProcesed) {
        String query = "UPDATE leaves SET status = ?, processedDate = ? WHERE leaveID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {             
            pst.setString(1, newStatus);
            pst.setTimestamp(2, Timestamp.valueOf(dateProcesed));
            pst.setInt(3, leaveID);
            return pst.executeUpdate() > 0;
                                                                 
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update status", e);
        }
    }
       
    private LeaveRequest createLeaveFromResultSet (ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("processedDate");
        LocalDateTime processedDate = (timestamp != null) ? timestamp.toLocalDateTime(): null;
        LeaveRequest leaveRequest = new LeaveRequest(
            rs.getInt("employeeID"),
            rs.getString("leaveType"),
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
