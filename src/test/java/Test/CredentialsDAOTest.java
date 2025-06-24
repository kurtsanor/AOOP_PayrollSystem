/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import Dao.CredentialsDAO;
import Service.TwoFactorAuthService;
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
    private static int employeeID;
    
    @BeforeAll
    public static void setUp() {
        dao = new CredentialsDAO();
        employeeID = 10001;
    }
    
    @Test
    public void testGetAuthenticatedID() throws SQLException {
        int authenticatedEmployeeID = dao.getAuthenticatedID("Garcia", "123");
        assertEquals(employeeID, authenticatedEmployeeID);
        assertTrue(authenticatedEmployeeID != -1, "Employe id of Garcia should not be -1");
    }
    
    @Test
    public void testIsEmployeePasswordCorrect() throws SQLException {
        String password = "123";
        boolean isCorrect = dao.isEmployeePasswordCorrect(employeeID, password);
        assertTrue(isCorrect, "Password should be correct (123)");
    }
    
    @Test
    public void testUpdatePasswordByEmployeeID() throws SQLException {
        String originalPassword = "123";
        String newPassword = "newpassword";
        boolean isUpdated = dao.updatePasswordByEmployeeID(employeeID, newPassword);
        assertTrue(isUpdated, "Password should be updated");
        
        boolean isUpdatedToOldPass = dao.updatePasswordByEmployeeID(employeeID, originalPassword);
        assertTrue(isUpdatedToOldPass);
    }
    
    @Test
    public void testHas2FAEnabled() throws SQLException {
        //enable 2fa for the employee
        String totpSecret =  TwoFactorAuthService.generateSecretKey();
        boolean updated  = dao.updateTotpSecret(employeeID, totpSecret);
        assertTrue(updated);
        
        // test if enabled
        boolean isEnabled = dao.has2FAEnabled(employeeID);
        assertTrue(isEnabled, "Employee should have enabled 2fa");
        
        // remove  the totp secret after testing
        boolean removed = dao.remove2FA(employeeID);
        assertTrue(removed);
    }
    
    @Test
    public void testGetTotpSecretByEmployeeID() throws SQLException {
        //enable 2fa for the employee
        String totpSecret =  TwoFactorAuthService.generateSecretKey();
        boolean updated  = dao.updateTotpSecret(employeeID, totpSecret);
        assertTrue(updated);
        
        // check if totp secret exists after enabling
        String secret = dao.getTotpSecretByEmployeeID(employeeID);
        assertNotNull(secret);
        assertTrue(!secret.isBlank());
        
        // remove  the totp secret after testing
        boolean removed = dao.remove2FA(employeeID);
        assertTrue(removed);
    }
    
    @Test
    public void testUpdateTotpSecret() throws SQLException {
        //enable 2fa for the employee
        String totpSecret =  TwoFactorAuthService.generateSecretKey();
        boolean updated  = dao.updateTotpSecret(employeeID, totpSecret);
        assertTrue(updated);
        
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
        
        // remove after testing
        boolean removed = dao.remove2FA(employeeID);
        assertTrue(removed);
    }
    
    @Test
    public void testRemove2FA() throws SQLException {
        //enable 2fa for the employee
        String totpSecret =  TwoFactorAuthService.generateSecretKey();
        boolean updated  = dao.updateTotpSecret(employeeID, totpSecret);
        assertTrue(updated);
        
        // check if secret exist
        String secret = dao.getTotpSecretByEmployeeID(employeeID);
        assertNotNull(secret);
        assertTrue(!secret.isBlank());
        
        // test the remove
       boolean removed = dao.remove2FA(employeeID);
        assertTrue(removed);
       
       // confirm if the secret isremvoed from the employee
       String secretAfterRemoving = dao.getTotpSecretByEmployeeID(employeeID);
       assertNull(secretAfterRemoving);
    }
    

        
}
