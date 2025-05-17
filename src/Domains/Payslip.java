/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domains;

import Model.Employee;

/**
 *
 * @author keith
 */
public class Payslip {
    private Employee employee;
    private YearPeriod period;
    private double workHours;
    private double basicSalary;
    private double sssDeduction;
    private double philhealthDeduction;
    private double taxDeduction;
    private double pagibigDeduction;
    private double grossPay;
    private double netPay;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    
    public Payslip (Employee employee, YearPeriod period, double workHours, double basicSalary, double sssDeduction, double philhealthDeduction, 
            double taxDeduction, double pagibigDeduction, double grossPay, double netPay, double riceSubsidy, double phoneAllowance, double clothingAllowance) {
        this.employee = employee;
        this.period = period;
        this.workHours = workHours;
        this.basicSalary = basicSalary;
        this.sssDeduction = sssDeduction;
        this.philhealthDeduction = philhealthDeduction;
        this.taxDeduction = taxDeduction;
        this.pagibigDeduction = pagibigDeduction;
        this.grossPay = grossPay;
        this.netPay = netPay;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
    }
    
    public double getTotalAllowance () {
        return riceSubsidy + phoneAllowance + clothingAllowance;
    }
    
    public double getTotalDeductions () {
        return sssDeduction + philhealthDeduction + taxDeduction + pagibigDeduction;
    }
    
    public Employee getEmployee() {
        return employee;
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
    
    public double getGrossPay () {
        return grossPay;
    }
    
    
}
