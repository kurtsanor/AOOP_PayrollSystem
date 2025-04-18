package oopClasses;
    
public class PayrollCalculator {
    
    public static double getGrossSalary (double basicSalary, double allowance) {
        return basicSalary + allowance;
    }
    
    public static double getNetSalary (double grossSalary) {
        return grossSalary - getTotalDeductions(grossSalary);   
    }
    
    public static double getTotalDeductions (double grossSalary) {
        double taxableIncome = getTaxableIncome(grossSalary);
        return getGovernmentDeductionsTotal(grossSalary) + TaxAndDeductionsModule.getWithholdingTax(taxableIncome);
    }
    
    public static double getGovernmentDeductionsTotal (double grossSalary) {
        return TaxAndDeductionsModule.getPagIbigDeduction(grossSalary)
             + TaxAndDeductionsModule.getPhilHealthDeduction(grossSalary)
             + TaxAndDeductionsModule.getSSSDeduction(grossSalary);
    }
    
    public static double getTaxableIncome (double grossSalary) {       
        return grossSalary - getGovernmentDeductionsTotal(grossSalary);      
    }
                 
}
