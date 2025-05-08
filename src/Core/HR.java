/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import Domains.AttendanceRecord;
import Domains.LeaveRequest;
import Domains.YearPeriod;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author keith
 */
public class HR extends Employee {
        
    public HR () {
        super();
    }
      
    public HR (Employee employee) {
        this.employeeID = employee.getID();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.position = employee.getPosition();
        this.status = employee.getStatus();
        this.birthday = employee.getBirthday();
        this.address = employee.getAddress();
        this.phoneNumber = employee.getPhoneNumber();
        this.sssNumber = employee.getSSSNumber();
        this.pagIbigNumber = employee.getPagibigNumber();
        this.tinNumber = employee.getTinNumber();
        this.hourlyRate = employee.getHourlyRate();
        this.philhealthNumber = employee.getPhilhealthNumber();
        this.role = employee.getRole();
        this.supervisor = employee.getSupervisor();
        this.basicSalary = employee.getBasicSalary();
        this.riceSubsidy = employee.getRiceSubsidy();
        this.phoneAllowance = employee.getPhoneAllowance();
        this.clothingAllowance = employee.getClothingAllowance();
        this.grossSemiMonthlyRate = employee.getGrossSemiMonthlyRate();
    }
    
    public boolean addEmployeeRecord (Employee employee) {
        try (Connection connection = DatabaseConnection.Connect()) {
            EmployeeDatabase employeeDB = new EmployeeDatabase(connection);
            return employeeDB.addEmployee(employee);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }       
    }
    
    public boolean updateEmployeeRecordByID (int employeeID, Employee updatedEmployee) {
        try (Connection connection = DatabaseConnection.Connect()) {
            EmployeeDatabase employeeDB = new EmployeeDatabase(connection);
            return employeeDB.editEmployee(employeeID, updatedEmployee);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }       
    }
    
    public boolean deleteEmployeeByID (int employeeID) {
        try (Connection connection = DatabaseConnection.Connect()) {
            EmployeeDatabase employeeDB = new EmployeeDatabase(connection);
            return employeeDB.deleteEmployee(employeeID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public Employee viewEmployeeByID (int employeeID) {
        try (Connection connection = DatabaseConnection.Connect()) {
            EmployeeDatabase employeeDB = new EmployeeDatabase(connection);
            return employeeDB.getEmployeeByID(employeeID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }       
    }
    
    public boolean updateLeaveStatus (int leaveID, String newStatus, LocalDateTime dateProcessed) {
        try (Connection connection = DatabaseConnection.Connect()) {
            LeaveDatabase leaveDB = new LeaveDatabase(connection);
            return leaveDB.updateStatus(leaveID, newStatus, dateProcessed);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  
        
    }
    
    public List<LeaveRequest> loadEmployeeLeaves () {
        try (Connection connection = DatabaseConnection.Connect()) {
            LeaveDatabase leaveDB = new LeaveDatabase(connection);
            return leaveDB.getAllRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }       
    }
    
    public LeaveRequest getLeavebyLeaveID (int leaveID) {
        try (Connection connection = DatabaseConnection.Connect()) {
            LeaveDatabase leaveDB = new LeaveDatabase(connection);
            return leaveDB.getLeaveByLeaveID(leaveID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } 
    }
    
    public List<AttendanceRecord> loadAttendanceByIdAndPeriod (int employeeID, YearPeriod period) {
        try (Connection connection = DatabaseConnection.Connect()) {
            AttendanceDatabase attendanceDB = new AttendanceDatabase(connection);
            return attendanceDB.getAttendanceByIdAndPeriod(employeeID, period);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }      
    }
    
    public List<Employee> loadEmployees () {
        try (Connection connection = DatabaseConnection.Connect()) {
            EmployeeDatabase employeeDB = new EmployeeDatabase(connection);
            return employeeDB.getAllEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }       
    }
    
    
    
    
    
    
    
    
    
    
    
}
