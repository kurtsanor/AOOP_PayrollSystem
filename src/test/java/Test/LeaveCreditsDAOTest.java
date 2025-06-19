/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import Dao.LeaveCreditsDAO;
import Model.LeaveBalance;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
/**
 *
 * @author admin
 */
public class LeaveCreditsDAOTest {
    private static LeaveCreditsDAO dao;
    
    @BeforeAll
    public static void  setUp() {
        dao = new LeaveCreditsDAO();
    }
    
    @Test
    public void testGetLeaveCreditsByEmpID_existingID() throws SQLException {
        LeaveBalance balance = dao.getLeaveCreditsByEmpID(10001);
        assertNotNull(balance, "Balance should not be null for this employee");
    }
    
    @Test
    public void testGetLeaveCreditsByEmpID_nonExistingID() throws SQLException {
        LeaveBalance balance = dao.getLeaveCreditsByEmpID(-9999); // invalid emp id
        assertNull(balance, "Should be null due to non existing id");
    }
    
    @Test
    public void testUpdateLeaveCreditsByEmpID() throws SQLException {
        int employeeID = 10001;
        // get and store the old leave credits of an employee
        LeaveBalance oldLeaveBalance = dao.getLeaveCreditsByEmpID(employeeID);
        int oldVacationCredits = oldLeaveBalance.getVacationLeaveCredits();
        int oldMedicalCredits = oldLeaveBalance.getMedicalLeaveCredits();
        int oldPersonalCredits = oldLeaveBalance.getPersonalLeaveCredits();
        
        System.out.println("OLD BALANCE OVERVIEW:\n vacation = " + oldVacationCredits + "\n medical = " + oldMedicalCredits + "\n personal = "+ oldPersonalCredits);
        
        // create a new leaveBalance object
        LeaveBalance newLeaveBalance = new LeaveBalance(employeeID, 5, 5, 5);
        boolean isUpdated = dao.updateLeaveCreditsByEmpID(employeeID, newLeaveBalance);
        assertTrue(isUpdated, "Leave Credits should be updated");
        
        LeaveBalance updatedBalanceFromDB = dao.getLeaveCreditsByEmpID(employeeID);      
        int newVacationCredits = updatedBalanceFromDB.getVacationLeaveCredits();
        int newMedicalCredits = updatedBalanceFromDB.getMedicalLeaveCredits();
        int newPersonalCredits = updatedBalanceFromDB.getPersonalLeaveCredits();
        
        System.out.println("NEW BALANCE OVERVIEW:\n vacation = " + newVacationCredits + "\n medical = " + newMedicalCredits + "\n personal = "+ newPersonalCredits);
                 
        // test if they are not equal anymroe
        assertNotEquals(oldVacationCredits, newVacationCredits);
        assertNotEquals(oldMedicalCredits, newMedicalCredits);
        assertNotEquals(oldPersonalCredits, newPersonalCredits);
        
        // revert the original leave balance
        boolean isReverted = dao.updateLeaveCreditsByEmpID(employeeID, oldLeaveBalance);
        assertTrue(isReverted);
             
    }
    
    @Test
    public void testGetAllEmployeesLeaveCredits() throws SQLException {
        List<LeaveBalance> balances = dao.getAllEmployeeLeaveCredits();
        assertTrue(!balances.isEmpty());
    }
}
