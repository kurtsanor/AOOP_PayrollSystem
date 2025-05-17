/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import Domains.PagibigMatrix;
import Domains.PhilhealthMatrix;
import Domains.SSSMatrix;
import Domains.TaxMatrix;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author keith
 */
public class DeductionCalculator {
    private static List<SSSMatrix> sssMatrix;
    private static List<PhilhealthMatrix> philhealthMatrix;
    private static List<PagibigMatrix> pagibigMatrix;
    private static List<TaxMatrix> taxMatrix;
    
    static {
        try {
            
            DeductionDAO dao = new DeductionDAO();
            sssMatrix = dao.getSSSMatrix();
            philhealthMatrix = dao.getPhilhealthMatrix();
            pagibigMatrix = dao.getPagibigMatrix();
            taxMatrix = dao.getTaxMatrix();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    public static double getSssContribution (double salary) {

            for (SSSMatrix bracket: sssMatrix) {
                if (isInSssBracket(salary, bracket)) {
                    return bracket.getContribution();
                }
            }     
        return -1;
    }
    
    private static boolean isInSssBracket (double salary, SSSMatrix bracket) {
        return (salary >= bracket.getMinSalary() && salary < bracket.getMaxSalary());
    }
    
    public static double getPhilhealthContribution (double salary) {

            for (PhilhealthMatrix bracket: philhealthMatrix) {
                if (isInPhilhealthBracket(salary, bracket)) {
                    return Math.min(salary * bracket.getPremiumRate(), bracket.getMaxContribution()) * bracket.getEmployeeShareRate();
                }
            }   
        return -1;
    }
    
    private static boolean isInPhilhealthBracket (double salary, PhilhealthMatrix bracket) {
        return (salary >= bracket.getMinSalary() && salary < bracket.getMaxSalary());
    }
    
    public static double getPagibigContribution (double salary) {

            for (PagibigMatrix bracket: pagibigMatrix) {
                if (isInPagibigBracket(salary, bracket)) {
                    return Math.min(salary * bracket.getEmployeeRate(), bracket.getMaxContribution());
                }
            }           
        return 0;
    }
    
    private static boolean isInPagibigBracket (double salary, PagibigMatrix bracket) {
        return (salary >= bracket.getMinSalary() && salary <= bracket.getMaxSalary());
    }
    
    public static double getTaxContribution (double taxableIncome) {
            for (TaxMatrix bracket: taxMatrix) {
                if (isInTaxBracket(taxableIncome, bracket)) {
                    return bracket.getBaseTax() + (taxableIncome - bracket.getExcessBase()) * bracket.getExcessRate();
                }
            } 
        return -1;
    }
    
    private static boolean isInTaxBracket (double taxableIncome, TaxMatrix bracket) {
        return (taxableIncome >= bracket.getMinSalary() && taxableIncome <= bracket.getMaxSalary());
    }

}
