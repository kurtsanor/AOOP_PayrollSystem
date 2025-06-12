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
}
