/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;

import Model.Employee;
import Dao.EmployeeDAO;
import Model.RegularEmployee;
import Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class EmployeeDAOTest {

    private static EmployeeDAO dao;

    @BeforeAll
    public static void setUp() {
        dao = new EmployeeDAO();
    }

    @Test
    public void testGetEmployeeByID() throws SQLException {
        Employee employee = dao.getEmployeeByID(10001);
        assertEquals("Garcia", employee.getLastName());
        assertEquals(535.71, employee.getHourlyRate());
        assertNotNull(employee);
    }

    @Test
    public void testGetAllEmployees() throws SQLException {
        List<Employee> employees = dao.getAllEmployees();
        assertFalse(employees.isEmpty());
    }

    public RegularEmployee createDummyEmployee() {
        return new RegularEmployee(0,
                "Employee test",
                "testSurname",
                "HR Manager",
                "Regular",
                LocalDate.of(2003, 05, 23),
                "Default Address",
                "123-456-789", 
                "12-1234567-1",
                "123456789123", "123-123-123-123", 123, "123456789123", "IT", 10001, 123, 123, 123, 123, 123);
    }

    @Test
    public void testAddEmployee() throws SQLException {
        Employee employee = createDummyEmployee();
        boolean added = dao.addEmployee(employee);
        assertTrue(added, "Employee should be added");

        // delete the temporary test data
        int lastInsertedId = getLastInsertedEmployeeID();
        boolean deleted = dao.deleteEmployee(lastInsertedId);
        assertTrue(deleted);
    }

    @Test
    public void testEditEmployee() throws SQLException {
        // 1. Retrieve and store original employee
        Employee originalEmployee = dao.getEmployeeByID(10001);
        assertNotNull(originalEmployee);
        assertEquals("Garcia", originalEmployee.getLastName()); // Just a quick check

        // 2. Create updated employee with ALL new fields
        Employee updatedEmployee = new RegularEmployee(
                originalEmployee.getID(),
                "UpdatedFirst", // First Name
                "UpdatedLast", // Last Name
                "HR Rank and File", // Position
                "Probationary", // Status
                LocalDate.of(2000, 1, 1), // Birthday
                "Updated Address", // Address
                "987-654-321", // Phone Number
                "UPDATED-SSS", // SSS
                "UPDATED-PAGIBIG", // Pag-IBIG
                "UPDATED-TIN", // TIN
                999.99, // Hourly Rate
                "UPDATED-PHILHEALTH", // PhilHealth
                "HR", // Role
                10001, // Supervisor ID
                50000, // Basic Salary
                1500, // Rice Subsidy
                1000, // Phone Allowance
                2000, // Clothing Allowance
                25000 // Gross Semi-Monthly
        );

        // 3. Update employee in database
        boolean isEditSuccessful = dao.editEmployee(10001, updatedEmployee);
        assertTrue(isEditSuccessful, "Employee should be edited");

        // 4. Validate updated fields
        Employee updatedFromDB = dao.getEmployeeByID(10001);
        assertEquals("UpdatedFirst", updatedFromDB.getFirstName());
        assertEquals("UpdatedLast", updatedFromDB.getLastName());
        assertEquals("HR Rank and File", updatedFromDB.getPosition());
        assertEquals("Probationary", updatedFromDB.getStatus());
        assertEquals(LocalDate.of(2000, 1, 1), updatedFromDB.getBirthday());
        assertEquals("Updated Address", updatedFromDB.getAddress());
        assertEquals("987-654-321", updatedFromDB.getPhoneNumber());
        assertEquals("UPDATED-SSS", updatedFromDB.getSSSNumber());
        assertEquals("UPDATED-PAGIBIG", updatedFromDB.getPagibigNumber());
        assertEquals("UPDATED-TIN", updatedFromDB.getTinNumber());
        assertEquals(999.99, updatedFromDB.getHourlyRate());
        assertEquals("UPDATED-PHILHEALTH", updatedFromDB.getPhilhealthNumber());
        assertEquals("HR", updatedFromDB.getRole());
        assertEquals(10001, updatedFromDB.getSupervisorID());
        assertEquals(50000, updatedFromDB.getBasicSalary());
        assertEquals(1500, updatedFromDB.getRiceSubsidy());
        assertEquals(1000, updatedFromDB.getPhoneAllowance());
        assertEquals(2000, updatedFromDB.getClothingAllowance());
        assertEquals(25000, updatedFromDB.getGrossSemiMonthlyRate());

        // 5. Revert employee to original
        boolean isRevertSuccessful = dao.editEmployee(10001, originalEmployee);
        assertTrue(isRevertSuccessful, "Original employee info should be restored");

        Employee reverted = dao.getEmployeeByID(10001);
        assertEquals(originalEmployee.getFirstName(), reverted.getFirstName());
        assertEquals(originalEmployee.getLastName(), reverted.getLastName());
        assertEquals(originalEmployee.getPosition(), reverted.getPosition());
        assertEquals(originalEmployee.getStatus(), reverted.getStatus());
        assertEquals(originalEmployee.getBirthday(), reverted.getBirthday());
        assertEquals(originalEmployee.getAddress(), reverted.getAddress());
        assertEquals(originalEmployee.getPhoneNumber(), reverted.getPhoneNumber());
        assertEquals(originalEmployee.getSSSNumber(), reverted.getSSSNumber());
        assertEquals(originalEmployee.getPagibigNumber(), reverted.getPagibigNumber());
        assertEquals(originalEmployee.getTinNumber(), reverted.getTinNumber());
        assertEquals(originalEmployee.getHourlyRate(), reverted.getHourlyRate());
        assertEquals(originalEmployee.getPhilhealthNumber(), reverted.getPhilhealthNumber());
        assertEquals(originalEmployee.getRole(), reverted.getRole());
        assertEquals(originalEmployee.getSupervisorID(), reverted.getSupervisorID());
        assertEquals(originalEmployee.getBasicSalary(), reverted.getBasicSalary());
        assertEquals(originalEmployee.getRiceSubsidy(), reverted.getRiceSubsidy());
        assertEquals(originalEmployee.getPhoneAllowance(), reverted.getPhoneAllowance());
        assertEquals(originalEmployee.getClothingAllowance(), reverted.getClothingAllowance());
        assertEquals(originalEmployee.getGrossSemiMonthlyRate(), reverted.getGrossSemiMonthlyRate());
    }

    @Test
    public void testDeleteEmployeByID() throws SQLException {
        // insert a new employeee first
        Employee employee = createDummyEmployee();
        boolean added = dao.addEmployee(employee);
        assertTrue(added);

        int lastInsertedId = getLastInsertedEmployeeID();

        // check if the new employee exists
        Employee newEmployee = dao.getEmployeeByID(lastInsertedId);
        assertNotNull(newEmployee, "New employee should exist");

        boolean isDeleted = dao.deleteEmployee(lastInsertedId);
        assertTrue(isDeleted, "Employee should be deleted");

        Employee deletedEmployee = dao.getEmployeeByID(lastInsertedId);
        assertNull(deletedEmployee, "Employee should not exist");
    }
    
    @Test
    public void testDuplicatedPhoneNumber() throws SQLException {
        // create a dummy employee with a phone number of "123-456-789"
        Employee employee = createDummyEmployee();
        boolean inserted = dao.addEmployee(employee);
        assertTrue(inserted);
        
        String phoneNumber = "123-456-789";
        
        boolean isExisting = dao.isExistingPhoneNumber(phoneNumber, null);
        assertTrue(isExisting);
        
        // delete the dummy data employee
        int lastInsertedId = getLastInsertedEmployeeID();
        boolean deleted = dao.deleteEmployee(lastInsertedId);
        assertTrue(deleted);
    }
    
    @Test
    public void testDuplicatedSssNumber() throws SQLException {
        // create a dummy employee with an sss number of "12-1234567-1"
        Employee employee = createDummyEmployee();
        boolean inserted = dao.addEmployee(employee);
        assertTrue(inserted);
        
        String sssNumber = "12-1234567-1";
        
        boolean isExisting = dao.isExistingSssNumber(sssNumber, null);
        assertTrue(isExisting);
        
        // delete the dummy data employee
        int lastInsertedId = getLastInsertedEmployeeID();
        boolean deleted = dao.deleteEmployee(lastInsertedId);
        assertTrue(deleted);
        
    }
    
    @Test
    public void testDuplicatedPhilhealthNumber() throws SQLException {
        // create a dummy employee with a philhealth number of "123456789123"
        Employee employee = createDummyEmployee();
        boolean inserted = dao.addEmployee(employee);
        assertTrue(inserted);
        
        String philhealthNumber = "123456789123";
        
        boolean isExisting = dao.isExistingPhilhealthNumber(philhealthNumber, null);
        assertTrue(isExisting);
        
        // delete the dummy data employee
        int lastInsertedId = getLastInsertedEmployeeID();
        boolean deleted = dao.deleteEmployee(lastInsertedId);
        assertTrue(deleted);
    }
    
    @Test
    public void testDuplicatedPagibigNumber() throws SQLException {
        // create a dummy employee with a pagibig number of "123456789123"
        Employee employee = createDummyEmployee();
        boolean inserted = dao.addEmployee(employee);
        assertTrue(inserted);
        
        String pagibigNumber = "123456789123";
        
        boolean isExisting = dao.isExistingPagibigNumber(pagibigNumber, null);
        assertTrue(isExisting);
        
        // delete the dummy data employee
        int lastInsertedId = getLastInsertedEmployeeID();
        boolean deleted = dao.deleteEmployee(lastInsertedId);
        assertTrue(deleted);
    }
    
    @Test
    public void testDuplicatedTinNumber() throws SQLException {
        // create a dummy employee with a Tin number of "123-123-123-123"
        Employee employee = createDummyEmployee();
        boolean inserted = dao.addEmployee(employee);
        assertTrue(inserted);
        
        String tinNumber = "123-123-123-123";
        
        boolean isExisting = dao.isExistingTinNumber(tinNumber, null);
        assertTrue(isExisting);
        
        // delete the dummy data employee
        int lastInsertedId = getLastInsertedEmployeeID();
        boolean deleted = dao.deleteEmployee(lastInsertedId);
        assertTrue(deleted);
    }

    // helper method for test data clean up
    private int getLastInsertedEmployeeID() throws SQLException {
        String query = "SELECT MAX(employeeID) FROM employees";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

}
