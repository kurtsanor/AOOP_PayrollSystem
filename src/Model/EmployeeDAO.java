package Model;

import DatabaseConnection.DatabaseConnection;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class EmployeeDAO {
    
    
    public EmployeeDAO () {} 
    
    public List <Employee> getAllEmployees () throws SQLException {
        List <Employee> employees = new ArrayList<>();
        String query = "SELECT "
                + "e.employeeID, "
                + "e.firstName, "
                + "e.lastName, "
                + "p.positionName, "
                + "st.statusName, "
                + "e.birthday, "
                + "e.address, "
                + "e.phoneNumber, "
                + "g.sssNumber, "
                + "g.pagibigNumber, "
                + "g.tinNumber, "
                + "s.hourlyRate, "
                + "g.philhealthNumber,"
                + "r.roleName, "
                + "e.supervisorID, "
                + "s.basicSalary, "
                + "SUM(CASE WHEN alt.allowanceTypeName = 'Rice Subsidy' THEN a.amount ELSE 0 END) as riceSubsidy, "
                + "SUM(CASE WHEN alt.allowanceTypeName = 'Phone' THEN a.amount ELSE 0 END) as phoneAllowance, "
                + "SUM(CASE WHEN alt.allowanceTypeName = 'Clothing' THEN a.amount ELSE 0 END) as clothingAllowance, "
                + "s.grossSemiMonthlyRate "
                + "FROM employees e JOIN status st ON e.statusID = st.statusID JOIN governmentNumber g ON e.employeeID = g.employeeID JOIN roles r ON e.roleID = r.roleID JOIN salary s ON e.employeeID = s.employeeID JOIN position p ON e.positionID = p.positionID JOIN allowance a ON e.employeeID = a.employeeID JOIN allowanceType alt ON a.allowanceTypeID = alt.allowanceTypeID GROUP BY e.employeeID, e.firstName, e.lastName, p.positionName, st.statusName, e.birthday, e.address, e.phoneNumber, g.sssNumber, g.philhealthNumber, g.pagibigNumber, g.tinNumber, s.hourlyRate, r.roleName, e.supervisorID, s.basicSalary, s.grossSemiMonthlyRate ORDER BY e.employeeID";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();           
            while (rs.next()) {
                Employee employee = EmployeeFactory.createEmployeeFromResultSet(rs);
                employees.add(employee);
            }                     
        } catch (SQLException e) {
            throw new SQLException("Failed to retrive all employees", e);
        }       
        return employees;
    }
    
    public Employee getEmployeeByID (int employeeID) throws SQLException {
        String query = "SELECT "
                + "e.employeeID, "
                + "e.firstName, "
                + "e.lastName, "
                + "p.positionName, "
                + "st.statusName, "
                + "e.birthday, "
                + "e.address, "
                + "e.phoneNumber, "
                + "g.sssNumber, "
                + "g.pagibigNumber, "
                + "g.tinNumber, "
                + "s.hourlyRate, "
                + "g.philhealthNumber,"
                + "r.roleName, "
                + "e.supervisorID, "
                + "s.basicSalary, "
                + "SUM(CASE WHEN alt.allowanceTypeName = 'Rice Subsidy' THEN a.amount ELSE 0 END) as riceSubsidy, "
                + "SUM(CASE WHEN alt.allowanceTypeName = 'Phone' THEN a.amount ELSE 0 END) as phoneAllowance, "
                + "SUM(CASE WHEN alt.allowanceTypeName = 'Clothing' THEN a.amount ELSE 0 END) as clothingAllowance, "
                + "s.grossSemiMonthlyRate "
                + "FROM employees e JOIN status st ON e.statusID = st.statusID JOIN governmentNumber g ON e.employeeID = g.employeeID JOIN roles r ON e.roleID = r.roleID JOIN salary s ON e.employeeID = s.employeeID JOIN position p ON e.positionID = p.positionID JOIN allowance a ON e.employeeID = a.employeeID JOIN allowanceType alt ON a.allowanceTypeID = alt.allowanceTypeID WHERE e.employeeID = ? GROUP BY e.employeeID, e.firstName, e.lastName, p.positionName, st.statusName, e.birthday, e.address, e.phoneNumber, g.sssNumber, g.philhealthNumber, g.pagibigNumber, g.tinNumber, s.hourlyRate, r.roleName, e.supervisorID, s.basicSalary, s.grossSemiMonthlyRate  ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {            
            pst.setInt(1, employeeID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return EmployeeFactory.createEmployeeFromResultSet(rs);
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
        
        String employeeQuery = "INSERT INTO employees (lastName, firstName, birthday, address, phoneNumber, statusID, positionID, roleID, supervisorID)"
                     + "VALUES (?,?,?,?,?,?,?,?,?)";
        
        String salaryQuery = "INSERT INTO salary (employeeID, basicSalary, grossSemiMonthlyRate, hourlyRate) VALUES (?,?,?,?)";
        
        String govNumberQuery = "INSERT INTO governmentNumber (employeeID, sssNumber, philhealthNumber, pagibigNumber, tinNumber) VALUES (?, ?, ?, ?, ?)";

        String allowanceQuery = "INSERT INTO allowance (employeeID, allowanceTypeID, amount) VALUES "
                + "(?,?,?),"
                + "(?,?,?),"
                + "(?,?,?)";
        
        try (Connection connection = DatabaseConnection.getConnection()){          
            // prevents sql queries from altering tables unless all other queries pass
            connection.setAutoCommit(false);
            try (PreparedStatement pstEmployee = connection.prepareStatement(employeeQuery,Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement pstGovNumber = connection.prepareStatement(govNumberQuery);                
                 PreparedStatement pstSalary = connection.prepareStatement(salaryQuery);
                 PreparedStatement pstAllowance = connection.prepareStatement(allowanceQuery)) {
                          
            pstEmployee.setString(1,  employee.getLastName());
            pstEmployee.setString(2, employee.getFirstName());
            pstEmployee.setDate(3, Date.valueOf(employee.getBirthday()));
            pstEmployee.setString(4, employee.getAddress());
            pstEmployee.setString(5, employee.getPhoneNumber());
            pstEmployee.setInt(6, statusID);
            pstEmployee.setInt(7, positionID);
            pstEmployee.setInt(8, roleID);
            pstEmployee.setInt(9, employee.getSupervisorID());
            
            int employeeAffectedRows = pstEmployee.executeUpdate();
            
            ResultSet employeeGeneratedKeys = pstEmployee.getGeneratedKeys();
            if (employeeGeneratedKeys.next()) {
                newEmployeeID = employeeGeneratedKeys.getInt(1);
            } else {
                throw new SQLException("Creating employee failed, no ID obtained");
            }
           
            pstGovNumber.setInt(1, newEmployeeID);
            pstGovNumber.setString(2, employee.getSSSNumber());
            pstGovNumber.setString(3, employee.getPhilhealthNumber());
            pstGovNumber.setString(4, employee.getPagibigNumber());
            pstGovNumber.setString(5, employee.getTinNumber());
           
            int govNumberAffectedRows = pstGovNumber.executeUpdate();
            
            pstSalary.setInt(1, newEmployeeID);
            pstSalary.setDouble(2, employee.getBasicSalary());
            pstSalary.setDouble(3, employee.getGrossSemiMonthlyRate());
            pstSalary.setDouble(4, employee.getHourlyRate());
            
            int salaryAffectedRows = pstSalary.executeUpdate();
            
            pstAllowance.setInt(1, newEmployeeID);
            pstAllowance.setInt(2, 1);
            pstAllowance.setDouble(3, employee.getRiceSubsidy());
            
            pstAllowance.setInt(4, newEmployeeID);
            pstAllowance.setInt(5, 2);
            pstAllowance.setDouble(6, employee.getPhoneAllowance());
            
            pstAllowance.setInt(7, newEmployeeID);
            pstAllowance.setInt(8, 3);
            pstAllowance.setDouble(9, employee.getClothingAllowance());
            
            int allowanceAffectedRows = pstAllowance.executeUpdate();
            
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
        
        String employeeQuery = "UPDATE employees SET lastName = ?, firstName = ?, birthday = ?, address = ?, phoneNumber = ?, statusID = ?, positionID = ?, roleID = ?, supervisorID = ? "
                     + "WHERE employeeID = ?";
        
        String salaryQuery = "UPDATE salary SET basicSalary = ?, grossSemiMonthlyRate = ?, hourlyRate = ? WHERE employeeID = ?";
        
        String govNumQuery = "UPDATE governmentNumber SET sssNumber = ?, philhealthNumber = ?,  pagibigNumber = ?, tinNumber = ? WHERE employeeID = ?";
        
        String allowanceQuery = "UPDATE allowance SET amount = ? WHERE employeeID = ? AND allowanceTypeID = ?";
        
        try (Connection connection = DatabaseConnection.getConnection()){
            // prevents sql queries from altering tables automatically
            connection.setAutoCommit(false);
            
            try (PreparedStatement employeePst = connection.prepareStatement(employeeQuery);
                 PreparedStatement salaryPst = connection.prepareStatement(salaryQuery);
                 PreparedStatement govNumPst = connection.prepareStatement(govNumQuery);
                 PreparedStatement allowancePst = connection.prepareStatement(allowanceQuery)) {
                
            employeePst.setString(1,  employee.getLastName());
            employeePst.setString(2, employee.getFirstName());
            employeePst.setDate(3, Date.valueOf(employee.getBirthday()));
            employeePst.setString(4, employee.getAddress());
            employeePst.setString(5, employee.getPhoneNumber());
            employeePst.setInt(6, statusID);
            employeePst.setInt(7, positionID);
            employeePst.setInt(8, roleID);
            employeePst.setInt(9, employee.getSupervisorID());
            employeePst.setInt(10, chosenEmployeeID);
            
            int employeeAffectedRows = employeePst.executeUpdate();
            
            salaryPst.setDouble(1, employee.getBasicSalary());
            salaryPst.setDouble(2, employee.getGrossSemiMonthlyRate());
            salaryPst.setDouble(3, employee.getHourlyRate());
            salaryPst.setInt(4, chosenEmployeeID);
            
            int salaryAffectedRows = salaryPst.executeUpdate();
            
            govNumPst.setString(1, employee.getSSSNumber());
            govNumPst.setString(2, employee.getPhilhealthNumber());
            govNumPst.setString(3, employee.getPagibigNumber());
            govNumPst.setString(4, employee.getTinNumber());
            govNumPst.setInt(5, chosenEmployeeID);
            
            int govNumAffectedRows = govNumPst.executeUpdate();
            
            allowancePst.setDouble(1, employee.getRiceSubsidy());
            allowancePst.setInt(2, chosenEmployeeID);
            allowancePst.setInt(3, 1);
            allowancePst.addBatch();
            
            allowancePst.setDouble(1, employee.getPhoneAllowance());
            allowancePst.setInt(2, chosenEmployeeID);
            allowancePst.setInt(3, 2);
            allowancePst.addBatch();
            
            allowancePst.setDouble(1, employee.getClothingAllowance());
            allowancePst.setInt(2, chosenEmployeeID);
            allowancePst.setInt(3, 3);
            allowancePst.addBatch();
            
            int res [] = allowancePst.executeBatch();
            
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
        String query = "DELETE FROM employees WHERE employeeID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, employeeId);
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Failed to delete record", e);
        }
    }
    
    public int getRoleID (String roleName) throws SQLException {
        String query = "SELECT roleID from roles WHERE roleName = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, roleName);
            ResultSet rs = pst.executeQuery();
            
            return rs.next()? rs.getInt("roleID") : -1;

        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve role id", e);
        }
    }
    
    public int getPositionID (String positionName) throws SQLException {
        String query = "SELECT positionID from position WHERE positionName = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, positionName);
            ResultSet rs = pst.executeQuery();
            
            return rs.next()? rs.getInt("positionID") : -1;

        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve position id", e);
        }
    }
    
    public int getStatusID (String statusName) throws SQLException {
        String query = "SELECT statusID from status WHERE statusName = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, statusName);
            ResultSet rs = pst.executeQuery();
            
            return rs.next()? rs.getInt("statusID") : -1;

        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve status id", e);
        }
    } 
    
}