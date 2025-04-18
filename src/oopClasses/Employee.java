package oopClasses;

import Domains.AttendanceRecord;
import Domains.LeaveRequest;
import Domains.Payslip;
import Domains.YearPeriod;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.sql.Connection;


public abstract class Employee implements EmployeeEssentials {
    
    protected int employeeID;
    protected String firstName;
    protected String lastName;
    protected String position;
    protected String status;
    protected LocalDate birthday;
    protected String address;
    protected String phoneNumber;
    protected String sssNumber;
    protected String pagIbigNumber;
    protected String tinNumber;
    protected double hourlyRate;
    protected String philhealthNumber;
    protected String role;
    protected String supervisor;
    protected double basicSalary;
    protected double riceSubsidy;
    protected double phoneAllowance;
    protected double clothingAllowance;
    protected double grossSemiMonthlyRate;
    
    
     
    public Employee () {}
         
    public void loadInformationFromDatabase (ResultSet rs) throws SQLException {
        this.employeeID = rs.getInt("employeeID");
        this.firstName = rs.getString("firstName");
        this.lastName = rs.getString("lastName");
        this.position = rs.getString("position");
        this.status = rs.getString("status");
        this.birthday = rs.getDate("birthday").toLocalDate();
        this.address = rs.getString("address");
        this.phoneNumber = rs.getString("phoneNumber");
        this.sssNumber = rs.getString("sssNumber");
        this.pagIbigNumber = rs.getString("pagibigNumber");
        this.tinNumber = rs.getString("tinNumber");
        this.hourlyRate = rs.getDouble("hourlyRate");
        this.philhealthNumber = rs.getString("philhealthNumber");
        this.role = rs.getString("role");
        this.supervisor = rs.getString("supervisor");
        this.basicSalary = rs.getDouble("basicSalary");
        this.riceSubsidy = rs.getDouble("riceSubsidy");
        this.phoneAllowance = rs.getDouble("phoneAllowance");
        this.clothingAllowance = rs.getDouble("clothingAllowance");
        this.grossSemiMonthlyRate = rs.getDouble("grossSemiMonthlyRate");
    }
    
    public int getID () { return employeeID;}
    public String getFirstName () { return firstName;}
    public String getLastName () { return lastName;}
    public String getPosition () { return position;}
    public String getStatus () { return status;}
    public LocalDate getBirthday () { return birthday;}
    public String getAddress () { return address;}
    public String getPhoneNumber () { return phoneNumber;}
    public String getPhilhealthNumber() { return philhealthNumber;}
    public String getSSSNumber () { return sssNumber;}
    public String getPagibigNumber () { return pagIbigNumber;}
    public String getTinNumber () { return tinNumber;}
    public double getHourlyRate() { return hourlyRate;}
    public String getRole() { return role;}
    public String getSupervisor() { return supervisor;}
    public double getBasicSalary() { return basicSalary;}
    public double getRiceSubsidy() { return riceSubsidy;}
    public double getPhoneAllowance() { return phoneAllowance;}
    public double getClothingAllowance() { return clothingAllowance;}
    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate;}
   
    @Override
    public boolean requestForLeave (LeaveRequest request) {  
        try (Connection connection = DatabaseConnection.Connect()) {
            LeaveDatabase leaveDB = new LeaveDatabase(connection);
            return leaveDB.submitLeaveRequest(request);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<AttendanceRecord> viewPersonalAttendance(int employeeID, YearPeriod period) {
        try (Connection connection = DatabaseConnection.Connect()) {
            AttendanceDatabase attendanceDB = new AttendanceDatabase(connection);
            return attendanceDB.getAttendanceByIdAndPeriod(employeeID, period);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    @Override
    public List<LeaveRequest> viewPersonalLeaves(int employeeID) {
        try (Connection connection = DatabaseConnection.Connect()) {
            LeaveDatabase leaveDB = new LeaveDatabase(connection);
            return leaveDB.getLeavesByEmployeeID(employeeID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }        
    }
    
    public Payslip viewPersonalSalary (YearPeriod period) {
        try (Connection connection = DatabaseConnection.Connect()) {
            AttendanceDatabase attendanceDB = new AttendanceDatabase(connection);
            double empWorkHours = HoursCalculator.calculateTotalHoursByPeriod(getID(), period, attendanceDB);
            double empBasicSalary = getHourlyRate() * empWorkHours;
            double totalAllowance = getRiceSubsidy() + getPhoneAllowance() + getClothingAllowance();
            double grossPay = PayrollCalculator.getGrossSalary(empBasicSalary, totalAllowance);
            double taxableIncome = PayrollCalculator.getTaxableIncome(grossPay);
            double sssDeduction = TaxAndDeductionsModule.getSSSDeduction(grossPay);
            double philhealthDeduction = TaxAndDeductionsModule.getPhilHealthDeduction(grossPay);
            double taxDeduction = TaxAndDeductionsModule.getWithholdingTax(taxableIncome);
            double pagibigDeduction = TaxAndDeductionsModule.getPagIbigDeduction(grossPay);
            double netPay = PayrollCalculator.getNetSalary(grossPay);
           
            Payslip payslip = new Payslip(
                getID(), 
                period,
                empWorkHours,
                empBasicSalary, 
                sssDeduction, 
                philhealthDeduction, 
                taxDeduction, 
                pagibigDeduction,
                grossPay,    
                netPay,
                getRiceSubsidy(),
                getPhoneAllowance(),
                getClothingAllowance());
            
            return payslip;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
                    
    }
    
   
    

    
    
          
    
    
    
    
}
