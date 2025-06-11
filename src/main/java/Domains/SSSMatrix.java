/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domains;

/**
 *
 * @author keith
 */
public class SSSMatrix {
    private double minSalary;
    private double maxSalary;
    private double contribution;

    public SSSMatrix(double minSalary, double maxSalary, double contribution) {
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.contribution = contribution;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public double getContribution() {
        return contribution;
    }
    
}
