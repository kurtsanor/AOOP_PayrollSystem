/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import Dao.LeaveDAO;
import Model.LeaveRequest;
import Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.function.Executable;
/**
 *
 * @author admin
 */
public class LeaveDAOTest {
    
    private static LeaveDAO dao;
    
    @BeforeAll
    public static void setUp() {
        dao = new LeaveDAO();
    }
    
    @Test
    public void testGetAllRecords() throws SQLException {
        List<LeaveRequest> requests = dao.getAllRecords();
        assertNotNull(requests, "List should not be empty");
        assertTrue(!requests.isEmpty());
    }
    
    @Test
    public void testGetLeaveByEmpID_existingID() throws SQLException {
        List<LeaveRequest> requests = dao.getLeavesByEmployeeID(10006);
        assertNotNull(requests);
        assertTrue(!requests.isEmpty());
    }
    
    @Test
    public void testGetLeaveByEmpID_nonExistingID() throws SQLException {
        List<LeaveRequest> requests = dao.getLeavesByEmployeeID(9999);
        assertTrue(requests.isEmpty());
    }
    
       
    @Test
    public void testGetPendingCount() throws SQLException {
        int count = dao.getPendingLeaveCountByEmployeeID(10006);
        assertTrue(count >0);
    }
    
    // helper method for cleaning up test data
    public boolean removeSubmittedLeave(LocalDate startDate) throws SQLException {
        String query = "DELETE FROM leaves WHERE startDate = ?";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(startDate));
            return stmt.executeUpdate() > 0;
        }
    }
    
    @Test
    public void testSubmitLeaveRequest_validRequest() throws SQLException {
        int employeeID = 10001;
        String leaveType = "Vacation";
        LocalDate start = LocalDate.of(2025, 8, 20);
        LocalDate end = LocalDate.of(2025, 8, 22);
        String status = "Pending";
        LocalDateTime submittedDate = LocalDateTime.now();
        String remarks = "Sick";
        LeaveRequest request = new LeaveRequest(employeeID, leaveType, start, end, status, submittedDate, remarks);
        
        boolean isSubmitted = dao.submitLeaveRequest(request);
        assertTrue(isSubmitted);
        
        // clean the test data
        removeSubmittedLeave(start);
    }   
    
    @Test
    public void testSubmitLeaveRequest_invalidRequest() throws SQLException {
        int employeeID = 99999;
        String leaveType = "Vacation";
        LocalDate start = LocalDate.of(2025, 8, 20);
        LocalDate end = LocalDate.of(2025, 8, 22);
        String status = "Pending";
        LocalDateTime submittedDate = LocalDateTime.now();
        String remarks = "Sick";
        LeaveRequest request = new LeaveRequest(employeeID, leaveType, start, end, status, submittedDate, remarks);
        
        
        Executable executable = new Executable() {
        @Override
        public void execute() throws Throwable {
            dao.submitLeaveRequest(request);
        }
    };
        assertThrows(SQLException.class, executable);
    }
    
    @Test
    public void testUpdateStatus() throws SQLException {
        LocalDateTime dateProcessed = LocalDateTime.now();
        boolean isUpdated = dao.updateStatus(1, "Approved", dateProcessed);
        assertTrue(isUpdated);
    }
    
    @Test
    public void testHasOverlappingApprovedLeave() throws SQLException {
        int employeeID = 10001;
        
        // create a leave request with an approved status
        String leaveType = "Vacation";
        LocalDate requestStart = LocalDate.of(2025, 8, 20);
        LocalDate requestEnd = LocalDate.of(2025, 8, 22);
        String status = "Approved";
        LocalDateTime submittedDate = LocalDateTime.now();
        String remarks = "Sick";
        LeaveRequest request = new LeaveRequest(employeeID, leaveType, requestStart, requestEnd, status, submittedDate, remarks);
        
        // insert in db
        boolean submitted = dao.submitLeaveRequest(request);
        assertTrue(submitted);
        
        // select a date where it will overlap the approved leave
        LocalDate start = LocalDate.of(2025, 8, 19);
        LocalDate end = LocalDate.of(2025, 8, 27);
        boolean hasOverlap = dao.hasOverlappingApprovedLeave(employeeID, start, end);
        assertTrue(hasOverlap, "This employee should already have an approved leave on these dates");
        
        // clean the test data
        boolean removed = removeSubmittedLeave(requestStart);
        assertTrue(removed);
    }
    
    @Test
    public void testGetApprovedCount() throws SQLException {
        int count = dao.getApprovedLeaveCountByEmployeeID(10006);
        assertTrue(count > 0);
    }
    
    @Test
    public void testGetDeniedCount() throws SQLException {
        int count = dao.getDeniedLeaveCountByEmployeeID(10006);
        assertTrue(count > 0);
    }
}
