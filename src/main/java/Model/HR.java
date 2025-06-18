/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Dao.AttendanceDAO;
import Dao.EmployeeDAO;
import Dao.LeaveDAO;
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
        this.supervisorID = employee.getSupervisorID();
        this.basicSalary = employee.getBasicSalary();
        this.riceSubsidy = employee.getRiceSubsidy();
        this.phoneAllowance = employee.getPhoneAllowance();
        this.clothingAllowance = employee.getClothingAllowance();
        this.grossSemiMonthlyRate = employee.getGrossSemiMonthlyRate();
    }
    
    public boolean addEmployeeRecord (Employee employee) {
        try {
            EmployeeDAO employeeDB = new EmployeeDAO();
            return employeeDB.addEmployee(employee);
        } catch (SQLException e) {
            e.printStackTrace();
        }       
        return false;
    }
    
    public boolean updateEmployeeRecordByID (int employeeID, Employee updatedEmployee) {
        try {
            EmployeeDAO employeeDB = new EmployeeDAO();
            return employeeDB.editEmployee(employeeID, updatedEmployee);
        } catch (SQLException e) {
            e.printStackTrace();
        }       
        return false;
    }
    
    public boolean deleteEmployeeByID (int employeeID) {
        try {
            EmployeeDAO employeeDB = new EmployeeDAO();
            return employeeDB.deleteEmployee(employeeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;      
    }
    
    public Employee viewEmployeeByID (int employeeID) {
        try {
            EmployeeDAO employeeDB = new EmployeeDAO();
            return employeeDB.getEmployeeByID(employeeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }       
        return null;
    }
    
    public boolean updateLeaveStatus (int leaveID, String newStatus, LocalDateTime dateProcessed) {
        try {
            LeaveDAO leaveDB = new LeaveDAO();
            return leaveDB.updateStatus(leaveID, newStatus, dateProcessed);
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        return false;       
    }
    
    public List<LeaveRequest> loadEmployeeLeaves () {
        try {
            LeaveDAO leaveDB = new LeaveDAO();
            return leaveDB.getAllRecords();
        } catch (SQLException e) {
            e.printStackTrace();
        }       
        return null;
    }
       
    public List<AttendanceRecord> loadAttendanceByIdAndPeriod (int employeeID, YearPeriod period) {
        try {
            AttendanceDAO attendanceDB = new AttendanceDAO();
            return attendanceDB.getAttendanceByIdAndPeriod(employeeID, period);
        } catch (SQLException e) {
            e.printStackTrace();
        }      
        return null;
    }
    
    public List<Employee> loadEmployees () {
        try {
            EmployeeDAO employeeDB = new EmployeeDAO();
            return employeeDB.getAllEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
        }       
        return null;
    }
    
    
    
    
    
    
    
    
    
    
    
}
