
package Model;

import Util.DatabaseConnection;
import Domains.LeaveRequest;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.CallableStatement;
import java.time.LocalDate;

public class LeaveDAO {   
    
    public LeaveDAO() {}
       
    public List <LeaveRequest> getAllRecords () throws SQLException {
        
        List <LeaveRequest> leaveRecords = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL leavesGetAll}")) {
            ResultSet rs = stmt.executeQuery();                                     
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
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL leavesGetByID(?)}")) {            
            stmt.setInt(1, employeeID);
            ResultSet rs = stmt.executeQuery();                                    
            while (rs.next()) {
                LeaveRequest leaveRequest = createLeaveFromResultSet(rs);
                leaveRecords.add(leaveRequest);
             }                                 
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve leave records by employee id", e);
        }
        
        return leaveRecords;
    }
             
    public boolean submitLeaveRequest (LeaveRequest leaveRequest) throws SQLException {
        int leaveTypeID = -1;
        
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement leaveTypeStmt = connection.prepareCall("{CALL leavetypeGetIdByName(?)}")) {
            leaveTypeStmt.setString(1, leaveRequest.getLeaveType());
            ResultSet rs = leaveTypeStmt.executeQuery();
            if (rs.next()) {
                leaveTypeID = rs.getInt("leaveTypeID");
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to get leave type id", e);
        }   
        
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement pst = connection.prepareCall("{CALL leavesInsert(?,?,?,?,?,?,?)}")) {
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
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL leavesUpdateStatus(?,?,?)}")) {             
            stmt.setString(1, newStatus);
            stmt.setTimestamp(2, Timestamp.valueOf(dateProcesed));
            stmt.setInt(3, leaveID);
            return stmt.executeUpdate() > 0;
                                                                 
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
    
    public boolean hasOverlappingApprovedLeave (int employeeID, LocalDate start, LocalDate end) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
            CallableStatement stmt = connection.prepareCall("CALL leavesHasOverlap(?,?,?)")) {
            stmt.setInt(1, employeeID);
            stmt.setDate(2, Date.valueOf(start));
            stmt.setDate(3, Date.valueOf(end));
            
            return stmt.executeQuery().next();
            
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
  
}
