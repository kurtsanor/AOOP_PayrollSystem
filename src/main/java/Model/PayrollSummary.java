/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author keith
 */
public class PayrollSummary {
    private double totalGrossIncome;
    private double totalSSS;
    private double totalPhilhealth;
    private double totalPagibig;
    private double totalTax;
    private double totalNetPay;

    public PayrollSummary(double totalGrossIncome, double totalSSS, double totalPhilhealth, double totalPagibig, double totalTax, double totalNetPay) {
        this.totalGrossIncome = totalGrossIncome;
        this.totalSSS = totalSSS;
        this.totalPhilhealth = totalPhilhealth;
        this.totalPagibig = totalPagibig;
        this.totalTax = totalTax;
        this.totalNetPay = totalNetPay;
    }

    public double getTotalGrossIncome() {
        return totalGrossIncome;
    }

    public double getTotalSSS() {
        return totalSSS;
    }

    public double getTotalPhilhealth() {
        return totalPhilhealth;
    }

    public double getTotalPagibig() {
        return totalPagibig;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public double getTotalNetPay() {
        return totalNetPay;
    }
    
    
    
}
