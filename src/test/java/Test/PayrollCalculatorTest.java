package Test;

import Service.PayrollCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PayrollCalculatorTest {

    private static final double BASIC_SALARY = 25000;
    private static final double ALLOWANCE = 5000;
    private static final double GROSS_SALARY = PayrollCalculator.getGrossSalary(BASIC_SALARY, ALLOWANCE);

    @Test
    public void testGetGrossSalary() {
        double gross = PayrollCalculator.getGrossSalary(BASIC_SALARY, ALLOWANCE);
        assertEquals(30000, gross);
    }

    @Test
    public void testGetGovernmentDeductionsTotal() {
        double total = PayrollCalculator.getGovernmentDeductionsTotal(GROSS_SALARY);
        assertTrue(total > 0);
    }

    @Test
    public void testGetTaxableIncome() {
        double taxable = PayrollCalculator.getTaxableIncome(GROSS_SALARY);
        double deductions = PayrollCalculator.getGovernmentDeductionsTotal(GROSS_SALARY);
        assertEquals(GROSS_SALARY - deductions, taxable, 0.01);
    }

    @Test
    public void testGetTotalDeductions() {
        double deductions = PayrollCalculator.getTotalDeductions(GROSS_SALARY);
        assertTrue(deductions > 0);
    }

    @Test
    public void testGetNetSalary() {
        double net = PayrollCalculator.getNetSalary(GROSS_SALARY);
        assertEquals(GROSS_SALARY - PayrollCalculator.getTotalDeductions(GROSS_SALARY), net, 0.01);
    }

    @Test
    public void testConvertToHourlyRate() {
        double rate = PayrollCalculator.convertToHourlyRate(BASIC_SALARY);
        assertEquals(BASIC_SALARY / (21.0 * 8.0), rate, 0.01);
    }

    @Test
    public void testConvertToSemiMonthlyRate() {
        double semiMonthly = PayrollCalculator.convertToSemiMonthlyRate(BASIC_SALARY);
        assertEquals(BASIC_SALARY / 2.0, semiMonthly, 0.01);
    }

    @Test
    public void testFormatAmount() {
        String formatted = PayrollCalculator.formatAmount(1234.5678);
        assertEquals("1234.57", formatted);
    }
}
