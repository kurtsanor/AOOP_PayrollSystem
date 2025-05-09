package Core;
    
public class PayrollCalculator {
    
    private static final int MONTHLY_WORKING_DAYS = 21;
    private static final int DAILY_WORKING_HOURS = 8;
    
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
    
    public static double convertToHourlyRate (double basicSalary) {
        return basicSalary / (MONTHLY_WORKING_DAYS * DAILY_WORKING_HOURS);
    }
    
    public static double convertToSemiMonthlyRate (double basicSalary) {
        return basicSalary / 2;
    }
    
    public static String formatAmount (double amount) {
        return String.format("%.2f", amount);
    }
                 
}
