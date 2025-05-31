package Model;

import Domains.AttendanceRecord;
import Domains.LeaveRequest;
import Domains.Payslip;
import Domains.YearPeriod;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;


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
    protected int supervisorID;
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
        this.position = rs.getString("positionName");
        this.status = rs.getString("statusName");
        this.birthday = rs.getDate("birthday").toLocalDate();
        this.address = rs.getString("address");
        this.phoneNumber = rs.getString("phoneNumber");
        this.sssNumber = rs.getString("sssNumber");
        this.pagIbigNumber = rs.getString("pagibigNumber");
        this.tinNumber = rs.getString("tinNumber");
        this.hourlyRate = rs.getDouble("hourlyRate");
        this.philhealthNumber = rs.getString("philhealthNumber");
        this.role = rs.getString("roleName");
        this.supervisorID = rs.getInt("supervisorID");
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
    public int getSupervisorID() { return supervisorID;}
    public double getBasicSalary() { return basicSalary;}
    public double getRiceSubsidy() { return riceSubsidy;}
    public double getPhoneAllowance() { return phoneAllowance;}
    public double getClothingAllowance() { return clothingAllowance;}
    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate;}
   
    @Override
    public boolean requestForLeave (LeaveRequest request) {  
        try {
            LeaveDAO leaveDB = new LeaveDAO();
            return leaveDB.submitLeaveRequest(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public List<AttendanceRecord> viewPersonalAttendance(int employeeID, YearPeriod period) {
        try {
            AttendanceDAO dao = new AttendanceDAO();
            return dao.getAttendanceByIdAndPeriod(employeeID, period);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;       
    }
    
    @Override
    public List<LeaveRequest> viewPersonalLeaves(int employeeID) {
        try {
            LeaveDAO dao = new LeaveDAO();
            return dao.getLeavesByEmployeeID(employeeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }        
        return null;
    }
    
    @Override
    public Payslip viewPersonalSalary (YearPeriod period) {
            AttendanceDAO attendanceDB = new AttendanceDAO();
            double empWorkHours = HoursCalculator.calculateTotalHoursByPeriod(getID(), period, attendanceDB);
            double empBasicSalary = getHourlyRate() * empWorkHours;
            double totalAllowance = getRiceSubsidy() + getPhoneAllowance() + getClothingAllowance();
            double grossPay = PayrollCalculator.getGrossSalary(empBasicSalary, totalAllowance);
            double taxableIncome = PayrollCalculator.getTaxableIncome(grossPay);
            double sssDeduction = DeductionCalculator.getSssContribution(grossPay);
            double philhealthDeduction = DeductionCalculator.getPhilhealthContribution(grossPay);
            double taxDeduction = DeductionCalculator.getTaxContribution(taxableIncome);
            double pagibigDeduction = DeductionCalculator.getPagibigContribution(grossPay);
            double netPay = PayrollCalculator.getNetSalary(grossPay);
           
            Payslip payslip = new Payslip(
                this, 
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
    }
    
    @Override
   public boolean timeIn (int employeeID, LocalDateTime dateTime) {
       try {
           AttendanceDAO attendanceDAO = new AttendanceDAO();
           return attendanceDAO.saveTimeIn(employeeID, dateTime);          
       } catch (SQLException e) {
           e.printStackTrace();
       }
        return false;
   }
       
    @Override
    public boolean timeOut (int employeeID, LocalDateTime dateTime) {
       try {
           AttendanceDAO attendanceDAO = new AttendanceDAO();
           return attendanceDAO.saveTimeOut(employeeID, dateTime);          
       } catch (SQLException e) {
           e.printStackTrace();
       }            
        return false;
   }
    

    
    
          
    
    
    
    
}
