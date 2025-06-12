/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;

import Model.Employee;
import Model.EmployeeDAO;
import Model.RegularEmployee;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
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
    }
    @Test
    public void testAddEmployee() throws SQLException {
        Employee employee = new RegularEmployee
        (0,
        "Employee test",
        "testSurname",
        "HR Manager",
        "Regular",
        LocalDate.of(2003, 05, 23),
        "Default Address",
        "123-123-123",
        "123",
        "123","123", 123, "123", "IT", 10001, 123, 123, 123, 123, 123);
        boolean added = dao.addEmployee(employee);
        assertTrue(added,"Employee should be added");
    }
    @Test
    public void testEditEmployee() throws SQLException {
        Employee originalEmployee = dao.getEmployeeByID(10001);
        String originalLastName = originalEmployee.getLastName();
        assertNotNull(originalEmployee);
        assertEquals("Garcia", originalLastName);
        
        Employee updatedEmployee = new RegularEmployee
        (originalEmployee.getID(),
        originalEmployee.getFirstName(),
        "Not Garcia",
        originalEmployee.getPosition(),
        originalEmployee.getStatus(),
        originalEmployee.getBirthday(),
        originalEmployee.getAddress(),
        originalEmployee.getPhoneNumber(),
        originalEmployee.getSSSNumber(),
        originalEmployee.getPagibigNumber(),
        originalEmployee.getTinNumber(),
        originalEmployee.getHourlyRate(),
        originalEmployee.getPhilhealthNumber(),
        originalEmployee.getRole(),
        originalEmployee.getSupervisorID(),
        originalEmployee.getBasicSalary(),
        originalEmployee.getRiceSubsidy(),
        originalEmployee.getPhoneAllowance(),
        originalEmployee.getClothingAllowance(),
        originalEmployee.getGrossSemiMonthlyRate());
        
        // edit selected employee details
        boolean isEditSuccessful = dao.editEmployee(10001, updatedEmployee);
        assertTrue(isEditSuccessful, "Employee should be edited");
        
        // test employee from the db
        Employee updatedEmployeeFromDAO = dao.getEmployeeByID(10001);
        assertNotEquals("Garcia", updatedEmployeeFromDAO.getLastName());
        assertEquals("Not Garcia", updatedEmployeeFromDAO.getLastName());
        
        // revert the original information of the employee
        boolean isRevertSuccessful = dao.editEmployee(10001, originalEmployee);
        assertTrue(isRevertSuccessful, "Original employee information should be back");
        
        Employee revertedEmployee = dao.getEmployeeByID(10001);
        assertEquals("Garcia", revertedEmployee.getLastName());
    }
    
    @Test
    public void testDeleteEmployeByID() throws SQLException {
        boolean isDeleted = dao.deleteEmployee(10060);
        assertTrue(isDeleted, "Employees should be deleted");
        
        Employee employee = dao.getEmployeeByID(10060);
        assertNull(employee, "Employees should not exist");
    }
        
}
