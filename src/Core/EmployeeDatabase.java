package Core;

import java.sql.Statement;
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
                + "s.hourlyRate, "
                + "e.philhealthNumber,"
                + "r.roleName, "
                + "e.supervisor, "
                + "s.basicSalary, "
                + "e.riceSubsidy, "
                + "e.phoneAllowance, "
                + "e.clothingAllowance, "
                + "s.grossSemiMonthlyRate "
                + "FROM employees e JOIN roles r ON e.roleID = r.roleID JOIN salary s ON e.employeeID = s.employeeID ORDER BY employeeID DESC";
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
                + "s.hourlyRate, "
                + "e.philhealthNumber,"
                + "r.roleName, "
                + "e.supervisor, "
                + "s.basicSalary, "
                + "e.riceSubsidy, "
                + "e.phoneAllowance, "
                + "e.clothingAllowance, "
                + "s.grossSemiMonthlyRate "
                + "FROM employees e JOIN roles r ON e.roleID = r.roleID JOIN salary s ON e.employeeID = s.employeeID WHERE e.employeeID = ?";
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
        int newEmployeeID = 0;
        int roleID = getRoleID(employee.getRole());
        
        String employeeQuery = "INSERT INTO employees (lastName, firstName, birthday, address, phoneNumber, sssNumber, philhealthNumber, tinNumber, pagibigNumber, status, position, roleID, supervisor, riceSubsidy, phoneAllowance, clothingAllowance) "
                     + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        String salaryQuery = "INSERT INTO salary (employeeID, basicSalary, grossSemiMonthlyRate, hourlyRate) VALUES (?,?,?,?)";
        try {
            // prevents sql queries from altering tables unless all other queries pass
            connection.setAutoCommit(false);
            try (PreparedStatement pstEmployee = connection.prepareStatement(employeeQuery,Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement pstSalary = connection.prepareStatement(salaryQuery)) {
                        
            pstEmployee.setString(1,  employee.getLastName());
            pstEmployee.setString(2, employee.getFirstName());
            pstEmployee.setDate(3, Date.valueOf(employee.getBirthday()));
            pstEmployee.setString(4, employee.getAddress());
            pstEmployee.setString(5, employee.getPhoneNumber());
            pstEmployee.setString(6, employee.getSSSNumber());
            pstEmployee.setString(7, employee.getPhilhealthNumber());
            pstEmployee.setString(8, employee.getTinNumber());
            pstEmployee.setString(9, employee.getPagibigNumber());
            pstEmployee.setString(10, employee.getStatus());
            pstEmployee.setString(11, employee.getPosition());
            pstEmployee.setInt(12, roleID);
            pstEmployee.setString(13, employee.getSupervisor());
            pstEmployee.setDouble(14, employee.getRiceSubsidy());
            pstEmployee.setDouble(15, employee.getPhoneAllowance());
            pstEmployee.setDouble(16, employee.getClothingAllowance());
            
            int employeeAffectedRows = pstEmployee.executeUpdate();
            
            ResultSet generatedKeys = pstEmployee.getGeneratedKeys();
            if (generatedKeys.next()) {
                newEmployeeID = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating employee failed, no ID obtained");
            }
            
            pstSalary.setInt(1, newEmployeeID);
            pstSalary.setDouble(2, employee.getBasicSalary());
            pstSalary.setDouble(3, employee.getGrossSemiMonthlyRate());
            pstSalary.setDouble(4, employee.getHourlyRate());
            
            int salaryAffectedRows = pstSalary.executeUpdate();
            
            connection.commit();
            
            return employeeAffectedRows > 0 && salaryAffectedRows > 0;    
                                      
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException("Transaction Failed, rolled back", e);
        }  
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add employee record", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to set auto commit to true",e);
            }
        }
            
    }
    
    public boolean editEmployee (int chosenEmployeeID, Employee employee) {
        int roleID = getRoleID(employee.getRole());
        String query = "UPDATE employees SET lastName = ?, firstName = ?, birthday = ?, address = ?, phoneNumber = ?, sssNumber = ?, philhealthNumber = ?, tinNumber = ?, pagibigNumber = ?, status = ?, position = ?, roleID = ?, supervisor = ?, basicSalary = ?, riceSubsidy = ?, phoneAllowance = ?, clothingAllowance = ?, grossSemiMonthlyRate = ?, hourlyRate = ? "
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
            pst.setInt(12, roleID);
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
    
    public int getRoleID (String roleName) {
        String query = "SELECT roleID from roles WHERE roleName = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, roleName);
            ResultSet rs = pst.executeQuery();
            
            return rs.next()? rs.getInt("roleID") : -1;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve role id", e);
        }
    }
}