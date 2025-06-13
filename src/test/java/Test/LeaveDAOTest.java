/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import Model.LeaveDAO;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
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
    public void testGetPendingCount() throws SQLException {
        int count = dao.getPendingLeaveCountByEmployeeID(10006);
        assertEquals(1, count);
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
