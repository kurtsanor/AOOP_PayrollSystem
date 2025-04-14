package oopClasses;

import java.time.LocalDate;

public class RegularEmployee extends Employee {
    
    public RegularEmployee () {
        super();
    }
    
    public RegularEmployee(int employeeID, String firstName, String lastName, String position, String status,
                           LocalDate birthday, String address, String phoneNumber, String sssNumber,
                           String pagIbigNumber, String tinNumber, double hourlyRate, String philhealthNumber, String role,
                           String supervisor, double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance,
                           double grossSemiMonthlyRate) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.status = status;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.pagIbigNumber = pagIbigNumber;
        this.tinNumber = tinNumber;
        this.hourlyRate = hourlyRate;
        this.philhealthNumber = philhealthNumber;
        this.role = role;
        this.supervisor = supervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
    }
    
    public RegularEmployee (Employee employee) {
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
    
    
    
    

     
}
