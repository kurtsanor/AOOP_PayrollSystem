/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import Domains.EmployeeMonthlyHoursKey;
import Domains.PayrollEntry;
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
            double sss = TaxAndDeductionsModule.getSSSDeduction(grossIncome);
            double philhealth = TaxAndDeductionsModule.getPhilHealthDeduction(grossIncome);
            double pagibig = TaxAndDeductionsModule.getPagIbigDeduction(grossIncome);
            double taxableIncome = PayrollCalculator.getTaxableIncome(grossIncome);
            double withholdingTax = TaxAndDeductionsModule.getWithholdingTax(taxableIncome);
            double netPay = PayrollCalculator.getNetSalary(grossIncome);
            
            return new PayrollEntry(employeeID, fullName, position, grossIncome, sss, philhealth, pagibig, withholdingTax, netPay);
    }
    
}
