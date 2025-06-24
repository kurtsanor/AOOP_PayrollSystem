/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;

import Model.Employee;
import Model.Finance;
import Model.HR;
import Model.IT;
import Model.RegularEmployee;
import Service.EmployeeFactory;
import Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author admin
 */
public class EmployeeFactoryTest {
    
    
    @Test
    public void testCreateEmployeeFromResultSet_HREmployee() throws SQLException {
        int employeeID = 10006;
        Employee employee = getEmployee(employeeID);
        assertTrue(employee instanceof HR);
    }
    
    @Test
    public void testCreateEmployeeFromResultSet_FinanceEmployee() throws SQLException {
        int employeeID = 10003;
        Employee employee = getEmployee(employeeID);
        assertTrue(employee instanceof Finance);
    }
    
    @Test
    public void testCreateEmployeeFromResultSet_ITEmployee() throws SQLException {
        int employeeID = 10005;
        Employee employee = getEmployee(employeeID);
        assertTrue(employee instanceof IT);
    }
    
    @Test
    public void testCreateEmployeeFromResultSet_RegularEmployee() throws SQLException {
        int employeeID = 10001;
        Employee employee = getEmployee(employeeID);
        assertTrue(employee instanceof RegularEmployee);
    }
    
    // helper method
    public Employee getEmployee(int employeeID) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("CALL employeesGetByID(?)")) {
            stmt.setInt(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return EmployeeFactory.createEmployeeFromResultSet(rs);
            }
            return null;
        }
    }

    
}
