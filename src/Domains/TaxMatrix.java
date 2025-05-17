/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domains;

/**
 *
 * @author keith
 */
public class TaxMatrix {
    private double minSalary;
    private double maxSalary;
    private double baseTax;
    private double excessRate;
    private double excessBase;

    public TaxMatrix(double minSalary, double maxSalary, double baseTax, double excessRate, double excessBase) {
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.baseTax = baseTax;
        this.excessRate = excessRate;
        this.excessBase = excessBase;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public double getBaseTax() {
        return baseTax;
    }

    public double getExcessRate() {
        return excessRate;
    }

    public double getExcessBase() {
        return excessBase;
    }
    
    
}
