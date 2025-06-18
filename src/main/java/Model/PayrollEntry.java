/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author keith
 */
public class PayrollEntry {
    private int employeeID;
    private String fullName;
    private String position;
    private double grossIncome;
    private double sss;
    private double philhealth;
    private double pagibig;
    private double withholdingTax;
    private double netPay;

    public PayrollEntry(int employeeID, String fullName, String position, double grossIncome, double sss, double philhealth, double pagibig, double withholdingTax, double netPay) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.position = position;
        this.grossIncome = grossIncome;
        this.sss = sss;
        this.philhealth = philhealth;
        this.pagibig = pagibig;
        this.withholdingTax = withholdingTax;
        this.netPay = netPay;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPosition() {
        return position;
    }

    public double getGrossIncome() {
        return grossIncome;
    }

    public double getSss() {
        return sss;
    }

    public double getPhilhealth() {
        return philhealth;
    }

    public double getPagibig() {
        return pagibig;
    }

    public double getWithholdingTax() {
        return withholdingTax;
    }

    public double getNetPay() {
        return netPay;
    }
    
}
