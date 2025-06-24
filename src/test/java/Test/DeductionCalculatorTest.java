/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import Service.DeductionCalculator;
import Service.PayrollCalculator;
/**
 *
 * @author admin
 */
public class DeductionCalculatorTest {
    
    private static final double SALARY = 25000;
    
    @Test
    public void testGetSSSContribution() {
        double contribution = DeductionCalculator.getSssContribution(SALARY);
        assertEquals(1125, contribution);
    }
    
    @Test
    public void testGetPhilhealthContribution() {
        double contribution = DeductionCalculator.getPhilhealthContribution(SALARY);
        assertEquals(375, contribution);
    }
    
    @Test
    public void testGetPagibigContribution() {
        double contribution = DeductionCalculator.getPagibigContribution(SALARY);
        assertEquals(100, contribution);
    }
    
    @Test
    public void testGetTaxContribution() {
        double taxableIncome = PayrollCalculator.getTaxableIncome(SALARY);
        
        double contribution = DeductionCalculator.getTaxContribution(taxableIncome);
        assertEquals(513.4, contribution);
    }
    @Test
    public void testSSSContributionInvalidSalary() {
        double contribution = DeductionCalculator.getSssContribution(-1000);
        assertEquals(-1, contribution, "Negative salary should return -1 from SSS");
    }

    @Test
    public void testPhilhealthContributionInvalidSalary() {
        double contribution = DeductionCalculator.getPhilhealthContribution(-1000);
        assertEquals(-1, contribution, "Negative salary should return -1 from PhilHealth");
    }

    @Test
    public void testTaxContributionInvalidSalary() {
        double contribution = DeductionCalculator.getTaxContribution(-10000);
        assertEquals(-1, contribution, "Negative salary should return -1 from Tax");
    }

    @Test
    public void testPagibigContributionZero() {
        double contribution = DeductionCalculator.getPagibigContribution(0);
        assertEquals(0, contribution, "Zero salary should return 0 Pag-IBIG");
    }
        
}
