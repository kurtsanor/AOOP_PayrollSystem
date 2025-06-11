/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;

import Model.Employee;
import Model.EmployeeDAO;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author admin
 */
public class EmployeeTest {
    
    private static EmployeeDAO dao;
    
    @BeforeAll
    public static void setUp() {
        dao = new EmployeeDAO();
    }
    
    @Test
    public void testGetEmployeeByID() throws SQLException {
        Employee employee = dao.getEmployeeByID(10001);
        assertEquals( "Garcia", employee.getLastName());
        assertEquals(employee.getHourlyRate(), 535.71);
        assertNotNull(employee);
    }
    
    @Test
    public void testGetAllEmployees() throws SQLException {
        List<Employee> employees = dao.getAllEmployees();
        assertFalse(employees.isEmpty());
        assertEquals(34, employees.size());
    }
        
}
