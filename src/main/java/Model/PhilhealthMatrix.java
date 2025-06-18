/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author keith
 */
public class PhilhealthMatrix {
    private double minSalary;
    private double maxSalary;
    private double premiumRate;
    private double maxContribution;
    private double employeeShareRate;

    public PhilhealthMatrix(double minSalary, double maxSalary, double premiumRate, double maxContribution, double employeeShareRate) {
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.premiumRate = premiumRate;
        this.maxContribution = maxContribution;
        this.employeeShareRate = employeeShareRate;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public double getPremiumRate() {
        return premiumRate;
    }

    public double getMaxContribution() {
        return maxContribution;
    }

    public double getEmployeeShareRate() {
        return employeeShareRate;
    }
    
    
}
