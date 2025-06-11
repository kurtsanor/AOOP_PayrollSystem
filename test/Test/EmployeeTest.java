package Test;


import Model.Employee;
import Model.EmployeeDAO;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin
 */
public class EmployeeTest {
    private static EmployeeDAO dao;
    
    @BeforeAll
    public static void setUp() throws SQLException {
        dao = new EmployeeDAO();
    }
    
    @Test
    public void testGetEmployeeByID() throws SQLException {
        Employee employee = dao.getEmployeeByID(10002);
        System.out.println(employee.getFirstName());
        assertEquals(employee.getFirstName(), "Manuel III");
        System.out.println("hello world");
        
    }
    
    @AfterAll
    public static void closing() {
        
    }
}
