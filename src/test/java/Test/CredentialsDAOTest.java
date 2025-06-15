/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import Model.CredentialsDAO;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
/**
 *
 * @author admin
 */
public class CredentialsDAOTest {
    
    private static CredentialsDAO dao;
    
    @BeforeAll
    public static void setUp() {
        dao = new CredentialsDAO();
    }
    
    @Test
    public void testGetAuthenticatedID() throws SQLException {
        int authenticatedEmployeeID = dao.getAuthenticatedID("Garcia", "123");
        assertEquals(10001, authenticatedEmployeeID);
        assertTrue(authenticatedEmployeeID != -1, "Employe id of Garcia should not be -1");
    }
    
    @Test
    public void testIsEmployeePasswordCorrect() throws SQLException {
        int employeeID = 10001;
        String password = "123";
        boolean isCorrect = dao.isEmployeePasswordCorrect(employeeID, password);
        assertTrue(isCorrect, "Password should be correct (123)");
    }
    
    @Test
    public void testUpdatePasswordByEmployeeID() throws SQLException {
        String originalPassword = "123";
        int employeeID = 10001;
        String newPassword = "newpassword";
        boolean isUpdated = dao.updatePasswordByEmployeeID(employeeID, newPassword);
        assertTrue(isUpdated, "Password should be updated");
        
        boolean isUpdatedToOldPass = dao.updatePasswordByEmployeeID(employeeID, originalPassword);
        assertTrue(isUpdatedToOldPass);
    }
    
    @Test
    public void testHas2FAEnabled() throws SQLException {
        boolean isEnabled = dao.has2FAEnabled(10006);
        assertTrue(isEnabled, "Employee should have enabled 2fa");
    }
    
    @Test
    public void testGetTotpSecretByEmployeeID() throws SQLException {
        String totpSecret = dao.getTotpSecretByEmployeeID(10006);
        assertNotNull(totpSecret, "Employee should have a totp secret");
    }
    
    @Test
    public void updateTotpSecret() throws SQLException {
        int employeeID = 10006;
        String originalTotp = dao.getTotpSecretByEmployeeID(employeeID);
        assertNotNull(originalTotp, "Employee should have a totp secret");
        
        boolean isUpdated = dao.updateTotpSecret(employeeID, "test123");
        assertTrue(isUpdated, "totp should be updated");
        String updatedTotp = dao.getTotpSecretByEmployeeID(employeeID);
        
        assertNotEquals(updatedTotp, originalTotp);
        
        // revert the old Totp
        boolean isReverted = dao.updateTotpSecret(employeeID, originalTotp);
        assertTrue(isReverted, "Totp should go back to original");
        
        String finalTotp = dao.getTotpSecretByEmployeeID(employeeID);
        assertEquals(originalTotp, finalTotp, "final totp should be equal to the original one");
    }
}
