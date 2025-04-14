/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domains;

/**
 *
 * @author keith
 */
public class Payslip {
    private int employeeID;
    private YearPeriod period;
    private double workHours;
    private double basicSalary;
    private double sssDeduction;
    private double philhealthDeduction;
    private double taxDeduction;
    private double pagibigDeduction;
    private double netPay;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    
    public Payslip (int employeeID, YearPeriod period, double workHours, double basicSalary, double sssDeduction, double philhealthDeduction, double taxDeduction, double pagibigDeduction, double netPay) {
        this.employeeID = employeeID;
        this.period = period;
        this.workHours = workHours;
        this.basicSalary = basicSalary;
        this.sssDeduction = sssDeduction;
        this.philhealthDeduction = philhealthDeduction;
        this.taxDeduction = taxDeduction;
        this.pagibigDeduction = pagibigDeduction;
        this.netPay = netPay;
    }
    
    public double getTotalAllowance () {
        return riceSubsidy + phoneAllowance + clothingAllowance;
    }
    
    public double getTotalDeductions () {
        return sssDeduction + philhealthDeduction + taxDeduction + pagibigDeduction;
    }
    
    public int getEmployeeID() {
        return employeeID;
    }

    public YearPeriod getPeriod() {
        return period;
    }
    
    public double getWorkHours () {
        return workHours;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getSssDeduction() {
        return sssDeduction;
    }

    public double getPhilhealthDeduction() {
        return philhealthDeduction;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public double getPagibigDeduction() {
        return pagibigDeduction;
    }
    
    public double getNetPay() {
        return netPay;
    }

    public double getRiceSubsidy() {
        return riceSubsidy;
    }

    public double getPhoneAllowance() {
        return phoneAllowance;
    }

    public double getClothingAllowance() {
        return clothingAllowance;
    }
    
    
}
