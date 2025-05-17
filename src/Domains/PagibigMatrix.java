/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domains;

/**
 *
 * @author keith
 */
public class PagibigMatrix {
    private double minSalary;
    private double maxSalary;
    private double employeeRate;
    private double maxContribution;

    public PagibigMatrix(double minSalary, double maxSalary, double employeeRate, double maxContribution) {
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.employeeRate = employeeRate;
        this.maxContribution = maxContribution;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public double getEmployeeRate() {
        return employeeRate;
    }

    public double getMaxContribution() {
        return maxContribution;
    }
    
}
