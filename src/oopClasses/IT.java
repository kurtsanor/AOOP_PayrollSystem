package oopClasses;

public class IT extends Employee {
    
    public IT () {
        super();
    }
    
    public IT (Employee employee) {
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
