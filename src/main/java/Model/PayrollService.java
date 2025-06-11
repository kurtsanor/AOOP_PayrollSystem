/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Domains.EmployeeMonthlyHoursKey;
import Domains.PayrollEntry;
import Domains.PayrollSummary;
import Domains.YearPeriod;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author keith
 */
public class PayrollService {
       
    public static List<PayrollEntry> computeBatchPayroll (List<Employee> employees, Map<EmployeeMonthlyHoursKey, Double> workHoursMap, YearPeriod period) {
        List<PayrollEntry> payrollEntries = new ArrayList<>();
        
        for (Employee employee: employees) {
            payrollEntries.add(createPayrollEntry(employee, workHoursMap, period));
        }
        return payrollEntries;       
    }
    
    private static PayrollEntry createPayrollEntry (Employee employee, Map<EmployeeMonthlyHoursKey, Double> workHoursMap, YearPeriod period) {
            int employeeID = employee.getID();
            String fullName = employee.getFirstName() + " " + employee.getLastName();
            String position = employee.getPosition();
            EmployeeMonthlyHoursKey key = new EmployeeMonthlyHoursKey(employeeID, period.getYear(), period.getMonth());
            double basicSalary = employee.getHourlyRate() * workHoursMap.getOrDefault(key,0.0);
            double totalAllowance = employee.getRiceSubsidy() + employee.getPhoneAllowance() + employee.getClothingAllowance();
            double grossIncome = basicSalary + totalAllowance;
            double sss = DeductionCalculator.getSssContribution(grossIncome);
            double philhealth = DeductionCalculator.getPhilhealthContribution(grossIncome);
            double pagibig = DeductionCalculator.getPagibigContribution(grossIncome);
            double taxableIncome = PayrollCalculator.getTaxableIncome(grossIncome);
            double withholdingTax = DeductionCalculator.getTaxContribution(taxableIncome);
            double netPay = PayrollCalculator.getNetSalary(grossIncome);
            
            return new PayrollEntry(employeeID, fullName, position, grossIncome, sss, philhealth, pagibig, withholdingTax, netPay);
    }
    
    public static PayrollSummary calculatePayrollSummary(List<PayrollEntry> payrollEntries) {
        double totalGrossIncome = 0;
        double totalSSS = 0;
        double totalPhilhealth = 0;
        double totalPagibig = 0;
        double totalTax = 0;
        double totalNetPay = 0;
        
        for (PayrollEntry entry: payrollEntries) {
            totalGrossIncome += entry.getGrossIncome();
            totalSSS += entry.getSss();
            totalPhilhealth += entry.getPhilhealth();
            totalPagibig += entry.getPagibig();
            totalTax += entry.getWithholdingTax();
            totalNetPay += entry.getNetPay();
        }
        
        return new PayrollSummary(totalGrossIncome, totalSSS, totalPhilhealth, totalPagibig, totalTax, totalNetPay);
    }
    
}
