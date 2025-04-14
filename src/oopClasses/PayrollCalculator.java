package oopClasses;
    
public class PayrollCalculator {
    
    public static double getGrossSalary (double basicSalary) {
        return basicSalary;
    }
    
    public static double getNetSalary (double basicSalary) {
        return basicSalary - getTotalDeductions(basicSalary);   
    }
    
    public static double getTotalDeductions (double basicSalary) {
        double taxableIncome = getTaxableIncome(basicSalary);
        return getGovernmentDeductionsTotal(basicSalary) + TaxAndDeductionsModule.getWithholdingTax(taxableIncome);
    }
    
    public static double getGovernmentDeductionsTotal (double basicSalary) {
        return TaxAndDeductionsModule.getPagIbigDeduction(basicSalary)
             + TaxAndDeductionsModule.getPhilHealthDeduction(basicSalary)
             + TaxAndDeductionsModule.getSSSDeduction(basicSalary);
    }
    
    public static double getTaxableIncome (double basicSalary) {       
        return basicSalary - getGovernmentDeductionsTotal(basicSalary);      
    }
                 
}
