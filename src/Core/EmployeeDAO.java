package Core;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class EmployeeDAO {
    
    private final Connection connection;
    
    public EmployeeDAO (Connection connection) {
        this.connection = connection;
    } 
    
    public List <Employee> getAllEmployees () {
        List <Employee> employees = new ArrayList<>();
        String query = "SELECT "
                + "e.employeeID, "
                + "e.firstName, "
                + "e.lastName, "
                + "p.positionName, "
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
                + "e.supervisorID, "
                + "s.basicSalary, "
                + "e.riceSubsidy, "
                + "e.phoneAllowance, "
                + "e.clothingAllowance, "
                + "s.grossSemiMonthlyRate "
                + "FROM employees e JOIN roles r ON e.roleID = r.roleID JOIN salary s ON e.employeeID = s.employeeID JOIN position p ON e.positionID = p.positionID ORDER BY employeeID ASC";
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
                + "p.positionName, "
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
                + "e.supervisorID, "
                + "s.basicSalary, "
                + "e.riceSubsidy, "
                + "e.phoneAllowance, "
                + "e.clothingAllowance, "
                + "s.grossSemiMonthlyRate "
                + "FROM employees e JOIN roles r ON e.roleID = r.roleID JOIN salary s ON e.employeeID = s.employeeID JOIN position p ON e.positionID = p.positionID WHERE e.employeeID = ?";
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
        int positionID = getPositionID(employee.getPosition());
        
        String employeeQuery = "INSERT INTO employees (lastName, firstName, birthday, address, phoneNumber, sssNumber, philhealthNumber, tinNumber, pagibigNumber, status, positionID, roleID, supervisor, riceSubsidy, phoneAllowance, clothingAllowance) "
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
            pstEmployee.setInt(11, positionID);
            pstEmployee.setInt(12, roleID);
            pstEmployee.setInt(13, employee.getSupervisorID());
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
        int positionID = getPositionID(employee.getPosition());
        String employeeQuery = "UPDATE employees SET lastName = ?, firstName = ?, birthday = ?, address = ?, phoneNumber = ?, sssNumber = ?, philhealthNumber = ?, tinNumber = ?, pagibigNumber = ?, status = ?, positionID = ?, roleID = ?, supervisorID = ?,  riceSubsidy = ?, phoneAllowance = ?, clothingAllowance = ? "
                     + "WHERE employeeID = ?";
        
        String salaryQuery = "UPDATE salary SET basicSalary = ?, grossSemiMonthlyRate = ?, hourlyRate = ? WHERE employeeID = ?";
        
        try {
            // prevents sql queries from altering tables unless all other queries pass
            connection.setAutoCommit(false);
            
            try (PreparedStatement employeePst = connection.prepareStatement(employeeQuery);
                  PreparedStatement salaryPst = connection.prepareStatement(salaryQuery) ) {
                
            employeePst.setString(1,  employee.getLastName());
            employeePst.setString(2, employee.getFirstName());
            employeePst.setDate(3, Date.valueOf(employee.getBirthday()));
            employeePst.setString(4, employee.getAddress());
            employeePst.setString(5, employee.getPhoneNumber());
            employeePst.setString(6, employee.getSSSNumber());
            employeePst.setString(7, employee.getPhilhealthNumber());
            employeePst.setString(8, employee.getTinNumber());
            employeePst.setString(9, employee.getPagibigNumber());
            employeePst.setString(10, employee.getStatus());
            employeePst.setInt(11, positionID);
            employeePst.setInt(12, roleID);
            employeePst.setInt(13, employee.getSupervisorID());
            employeePst.setDouble(14, employee.getRiceSubsidy());
            employeePst.setDouble(15, employee.getPhoneAllowance());
            employeePst.setDouble(16, employee.getClothingAllowance());
            employeePst.setInt(17, chosenEmployeeID);
            
            int employeeAffectedRows = employeePst.executeUpdate();
            
            salaryPst.setDouble(1, employee.getBasicSalary());
            salaryPst.setDouble(2, employee.getGrossSemiMonthlyRate());
            salaryPst.setDouble(3, employee.getHourlyRate());
            salaryPst.setInt(4, chosenEmployeeID);
            
            int salaryAffectedRows = salaryPst.executeUpdate();
            
            connection.commit();
            
            return employeeAffectedRows > 0 && salaryAffectedRows > 0;
            
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException("Transaction failed, rolled back", e);
        }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to edit employee record",e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {             
                throw new RuntimeException("Failed to set auto commit to true", e);
            }
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
    
    public int getPositionID (String positionName) {
        String query = "SELECT positionID from position WHERE positionName = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, positionName);
            ResultSet rs = pst.executeQuery();
            
            return rs.next()? rs.getInt("positionID") : -1;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve position id", e);
        }
    }
    
}