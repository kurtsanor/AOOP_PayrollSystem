package oopClasses;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class EmployeeDatabase {
    
    private final Connection connection;
    
    public EmployeeDatabase (Connection connection) {
        this.connection = connection;
    } 
    
    public List <Employee> getAllEmployees () {
        List <Employee> employees = new ArrayList<>();
        String query = "SELCT "
                + "e.employeeID, "
                + "e.firstName, "
                + "e.lastName, "
                + "e.position, "
                + "e.status, "
                + "e.birthday, "
                + "e.address, "
                + "e.phoneNumber, "
                + "e.sssNumber, "
                + "e.pagibigNumber, "
                + "e.tinNumber, "
                + "e.hourlyRate, "
                + "e.philhealthNumber,"
                + "r.roleName, "
                + "e.supervisor, "
                + "e.basicSalary, "
                + "e.riceSubsidy, "
                + "e.phoneAllowance, "
                + "e.clothingAllowance, "
                + "e.grossSemiMonthlyRate "
                + "FROM employees e JOIN roles r ON e.roleID = r.roleID";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();           
            while (rs.next()) {
                Employee employee = EmployeeFactory.createEmployeeFromResultSet(rs);
                employees.add(employee);
            }                     
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrive all employees", e);
        }       
        return employees;
    }
    
    public List <Employee> searchEmployeeByNameOrID (String searchInput) {
        String query = "SELECT * FROM employees WHERE employeeID = ? OR LOWER(CONCAT(firstName, ' ', lastName)) LIKE ?";
        List <Employee> result = new ArrayList<>();
        
        try (PreparedStatement pst = connection.prepareStatement(query)) {         
            String searchPattern = "%" + searchInput.toLowerCase() + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            ResultSet rs = pst.executeQuery();
                    
            while (rs.next()) {
                Employee employee = EmployeeFactory.createEmployeeFromResultSet(rs);
                result.add(employee);      
            }                                          
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search employee records", e);
        }
        
        return result;
    }
     
    public Employee getEmployeeByID (int employeeID) {
        String query = "SELECT "
                + "e.employeeID, "
                + "e.firstName, "
                + "e.lastName, "
                + "e.position, "
                + "e.status, "
                + "e.birthday, "
                + "e.address, "
                + "e.phoneNumber, "
                + "e.sssNumber, "
                + "e.pagibigNumber, "
                + "e.tinNumber, "
                + "e.hourlyRate, "
                + "e.philhealthNumber,"
                + "r.roleName, "
                + "e.supervisor, "
                + "e.basicSalary, "
                + "e.riceSubsidy, "
                + "e.phoneAllowance, "
                + "e.clothingAllowance, "
                + "e.grossSemiMonthlyRate "
                + "FROM employees e JOIN roles r ON e.roleID = r.roleID WHERE e.employeeID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {            
            pst.setInt(1, employeeID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return EmployeeFactory.createEmployeeFromResultSet(rs);
            }                                              
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employee info", e);
        }
        return null;
    }
        
    public boolean addEmployee (Employee employee) {     
        String query = "INSERT INTO employees (lastName, firstName, birthday, address, phoneNumber, sssNumber, philhealthNumber, tinNumber, pagibigNumber, status, position, role, supervisor, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlyRate, hourlyRate) "
                     + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
        try (PreparedStatement pst = connection.prepareStatement(query)) {                      
            pst.setString(1,  employee.getLastName());
            pst.setString(2, employee.getFirstName());
            pst.setDate(3, Date.valueOf(employee.getBirthday()));
            pst.setString(4, employee.getAddress());
            pst.setString(5, employee.getPhoneNumber());
            pst.setString(6, employee.getSSSNumber());
            pst.setString(7, employee.getPhilhealthNumber());
            pst.setString(8, employee.getTinNumber());
            pst.setString(9, employee.getPagibigNumber());
            pst.setString(10, employee.getStatus());
            pst.setString(11, employee.getPosition());
            pst.setString(12, employee.getRole());
            pst.setString(13, employee.getSupervisor());
            pst.setDouble(14, employee.getBasicSalary());
            pst.setDouble(15, employee.getRiceSubsidy());
            pst.setDouble(16, employee.getPhoneAllowance());
            pst.setDouble(17, employee.getClothingAllowance());
            pst.setDouble(18, employee.getGrossSemiMonthlyRate());
            pst.setDouble(19, employee.getHourlyRate());
                   
            return pst.executeUpdate() > 0;                           
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add employee record", e);
        }      
    }
    
    public boolean editEmployee (int chosenEmployeeID, Employee employee) { 
        String query = "UPDATE employees SET lastName = ?, firstName = ?, birthday = ?, address = ?, phoneNumber = ?, sssNumber = ?, philhealthNumber = ?, tinNumber = ?, pagibigNumber = ?, status = ?, position = ?, role = ?, supervisor = ?, basicSalary = ?, riceSubsidy = ?, phoneAllowance = ?, clothingAllowance = ?, grossSemiMonthlyRate = ?, hourlyRate = ? "
                     + "WHERE employeeID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1,  employee.getLastName());
            pst.setString(2, employee.getFirstName());
            pst.setDate(3, Date.valueOf(employee.getBirthday()));
            pst.setString(4, employee.getAddress());
            pst.setString(5, employee.getPhoneNumber());
            pst.setString(6, employee.getSSSNumber());
            pst.setString(7, employee.getPhilhealthNumber());
            pst.setString(8, employee.getTinNumber());
            pst.setString(9, employee.getPagibigNumber());
            pst.setString(10, employee.getStatus());
            pst.setString(11, employee.getPosition());
            pst.setString(12, employee.getRole());
            pst.setString(13, employee.getSupervisor());
            pst.setDouble(14, employee.getBasicSalary());
            pst.setDouble(15, employee.getRiceSubsidy());
            pst.setDouble(16, employee.getPhoneAllowance());
            pst.setDouble(17, employee.getClothingAllowance());
            pst.setDouble(18, employee.getGrossSemiMonthlyRate());
            pst.setDouble(19, employee.getHourlyRate());
            pst.setInt(20,    chosenEmployeeID);
            
            return pst.executeUpdate() > 0;                       
        } catch (SQLException e) {
            throw new RuntimeException("Failed to edit record", e);
        }
    }
    
    public boolean deleteEmployee (int employeeId) {
        String query = "DELETE FROM employees WHERE employeeID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, employeeId);
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete record", e);
        }
    }      
}