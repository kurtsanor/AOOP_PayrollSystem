/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import Model.DeductionCalculator;
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
        double sss = DeductionCalculator.getSssContribution(SALARY);
        double philhealth = DeductionCalculator.getPhilhealthContribution(SALARY);
        double pagibig = DeductionCalculator.getPagibigContribution(SALARY);
        
        double taxableIncome = SALARY - (sss + philhealth + pagibig);
        
        double contribution = DeductionCalculator.getTaxContribution(taxableIncome);
        assertEquals(513.4, contribution);
    }
        
}
