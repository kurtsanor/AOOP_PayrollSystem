package Model;

import Util.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Types;

public class EmployeeDAO {
    
    
    public EmployeeDAO () {} 
    
    public List <Employee> getAllEmployees () throws SQLException {
        List <Employee> employees = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
            CallableStatement stmt = connection.prepareCall("{CALL employeesGetAll()}");
            ResultSet rs = stmt.executeQuery()) {
                       
            while (rs.next()) {
                Employee employee = EmployeeFactory.createEmployeeFromResultSet(rs);
                employees.add(employee);
            }                     
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve all employees", e);
        }       
        return employees;
    }
    
    public Employee getEmployeeByID (int employeeID) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
            CallableStatement stmt = connection.prepareCall("{CALL employeesGetByID(?)}")) {            
            stmt.setInt(1, employeeID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return EmployeeFactory.createEmployeeFromResultSet(rs);
                }
            }       
                                                   
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch employee info", e);
        }
        return null;
    }
        
    public boolean addEmployee (Employee employee) throws SQLException {
        int newEmployeeID = 0;
        int roleID = getRoleID(employee.getRole());
        int positionID = getPositionID(employee.getPosition());
        int statusID = getStatusID(employee.getStatus());
        
        try (Connection connection = DatabaseConnection.getConnection()){          
            // prevents sql queries from altering tables unless all other queries pass
            connection.setAutoCommit(false);
            try (CallableStatement stmtEmployee = connection.prepareCall("{CALL employeesInsert(?,?,?,?,?,?,?,?,?,?)}");
                 CallableStatement stmtGovNumber = connection.prepareCall("{CALL GovNumInsert(?,?,?,?,?)}");                
                 CallableStatement stmtSalary = connection.prepareCall("{CALL salaryInsert(?,?,?,?)}");
                 CallableStatement stmtAllowance = connection.prepareCall("{CALL allowanceInsert(?,?,?,?,?,?,?,?,?)}")) {
                          
            stmtEmployee.setString(1,  employee.getLastName());
            stmtEmployee.setString(2, employee.getFirstName());
            stmtEmployee.setDate(3, Date.valueOf(employee.getBirthday()));
            stmtEmployee.setString(4, employee.getAddress());
            stmtEmployee.setString(5, employee.getPhoneNumber());
            stmtEmployee.setInt(6, statusID);
            stmtEmployee.setInt(7, positionID);
            stmtEmployee.setInt(8, roleID);
            stmtEmployee.setInt(9, employee.getSupervisorID());
            stmtEmployee.registerOutParameter(10, Types.INTEGER);
            
            int employeeAffectedRows = stmtEmployee.executeUpdate();
            
            newEmployeeID = stmtEmployee.getInt(10);
         
            stmtGovNumber.setInt(1, newEmployeeID);
            stmtGovNumber.setString(2, employee.getSSSNumber());
            stmtGovNumber.setString(3, employee.getPhilhealthNumber());
            stmtGovNumber.setString(4, employee.getPagibigNumber());
            stmtGovNumber.setString(5, employee.getTinNumber());
           
            int govNumberAffectedRows = stmtGovNumber.executeUpdate();
            
            stmtSalary.setInt(1, newEmployeeID);
            stmtSalary.setDouble(2, employee.getBasicSalary());
            stmtSalary.setDouble(3, employee.getGrossSemiMonthlyRate());
            stmtSalary.setDouble(4, employee.getHourlyRate());
            
            int salaryAffectedRows = stmtSalary.executeUpdate();
            
            stmtAllowance.setInt(1, newEmployeeID);
            stmtAllowance.setInt(2, 1);
            stmtAllowance.setDouble(3, employee.getRiceSubsidy());
            
            stmtAllowance.setInt(4, newEmployeeID);
            stmtAllowance.setInt(5, 2);
            stmtAllowance.setDouble(6, employee.getPhoneAllowance());
            
            stmtAllowance.setInt(7, newEmployeeID);
            stmtAllowance.setInt(8, 3);
            stmtAllowance.setDouble(9, employee.getClothingAllowance());
            
            int allowanceAffectedRows = stmtAllowance.executeUpdate();
            
            connection.commit();
            
            return employeeAffectedRows > 0 && salaryAffectedRows > 0 && govNumberAffectedRows > 0 && allowanceAffectedRows > 0;    
                                      
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction Failed, rolled back", e);
        }  
            
        } catch (SQLException e) {
            throw new SQLException("Failed to add employee record", e);
        }           
    }
    
    public boolean editEmployee (int chosenEmployeeID, Employee employee) throws SQLException {
        int roleID = getRoleID(employee.getRole());
        int positionID = getPositionID(employee.getPosition());
        int statusID = getStatusID(employee.getStatus());
        
        try (Connection connection = DatabaseConnection.getConnection()){
            // prevents sql queries from altering tables automatically
            connection.setAutoCommit(false);
            
            try (CallableStatement stmtEmployee = connection.prepareCall("{CALL employeesUpdate(?,?,?,?,?,?,?,?,?,?)}");
                 PreparedStatement stmtSalary = connection.prepareStatement("{CALL salaryUpdate(?,?,?,?)}");
                 PreparedStatement stmtGovNum = connection.prepareStatement("{CALL govNumUpdate(?,?,?,?,?)}");
                 PreparedStatement stmtAllowance = connection.prepareStatement("{CALL allowanceUpdate(?,?,?)}")) {
                
            stmtEmployee.setString(1,  employee.getLastName());
            stmtEmployee.setString(2, employee.getFirstName());
            stmtEmployee.setDate(3, Date.valueOf(employee.getBirthday()));
            stmtEmployee.setString(4, employee.getAddress());
            stmtEmployee.setString(5, employee.getPhoneNumber());
            stmtEmployee.setInt(6, statusID);
            stmtEmployee.setInt(7, positionID);
            stmtEmployee.setInt(8, roleID);
            stmtEmployee.setInt(9, employee.getSupervisorID());
            stmtEmployee.setInt(10, chosenEmployeeID);
            
            int employeeAffectedRows = stmtEmployee.executeUpdate();
            
            stmtSalary.setDouble(1, employee.getBasicSalary());
            stmtSalary.setDouble(2, employee.getGrossSemiMonthlyRate());
            stmtSalary.setDouble(3, employee.getHourlyRate());
            stmtSalary.setInt(4, chosenEmployeeID);
            
            int salaryAffectedRows = stmtSalary.executeUpdate();
            
            stmtGovNum.setString(1, employee.getSSSNumber());
            stmtGovNum.setString(2, employee.getPhilhealthNumber());
            stmtGovNum.setString(3, employee.getPagibigNumber());
            stmtGovNum.setString(4, employee.getTinNumber());
            stmtGovNum.setInt(5, chosenEmployeeID);
            
            int govNumAffectedRows = stmtGovNum.executeUpdate();
            
            stmtAllowance.setDouble(1, employee.getRiceSubsidy());
            stmtAllowance.setInt(2, chosenEmployeeID);
            stmtAllowance.setInt(3, 1);
            stmtAllowance.addBatch();
            
            stmtAllowance.setDouble(1, employee.getPhoneAllowance());
            stmtAllowance.setInt(2, chosenEmployeeID);
            stmtAllowance.setInt(3, 2);
            stmtAllowance.addBatch();
            
            stmtAllowance.setDouble(1, employee.getClothingAllowance());
            stmtAllowance.setInt(2, chosenEmployeeID);
            stmtAllowance.setInt(3, 3);
            stmtAllowance.addBatch();
            
            int res [] = stmtAllowance.executeBatch();
            
            connection.commit();
            
            return employeeAffectedRows > 0 && salaryAffectedRows > 0 && govNumAffectedRows > 0;
            
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Transaction failed, rolled back", e);
        }
            
        } catch (SQLException e) {
            throw new SQLException("Failed to edit employee record",e); 
        }
    }
    
    public boolean deleteEmployee (int employeeId) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL employeesDelete(?)}")) {
            stmt.setInt(1, employeeId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Failed to delete record", e);
        }
    }
    
    public int getRoleID (String roleName) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL rolesGetID(?)}")) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next()? rs.getInt("roleID") : -1;

        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve role id", e);
        }
    }
    
    public int getPositionID (String positionName) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL positionGetID(?)}")) {
            stmt.setString(1, positionName);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next()? rs.getInt("positionID") : -1;

        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve position id", e);
        }
    }
    
    public int getStatusID (String statusName) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL statusGetID(?)}")) {
            stmt.setString(1, statusName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next()? rs.getInt("statusID") : -1;
            }
                     
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve status id", e);
        }
    } 
    
}