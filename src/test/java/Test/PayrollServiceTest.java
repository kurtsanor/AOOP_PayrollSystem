package Test;

import Model.Employee;
import Model.EmployeeMonthlyHoursKey;
import Model.PayrollEntry;
import Model.PayrollSummary;
import Model.Payslip;
import Model.RegularEmployee;
import Model.YearPeriod;
import Service.PayrollService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PayrollServiceTest {

    @Test
    public void testComputeBatchPayroll() {
        RegularEmployee employee = new RegularEmployee(
            10001, "TestName", "TestSurname", "HR Rank and File", "Regular",
            LocalDate.of(1995, 1, 1), "123 Street", "123-456-789", "12-1234567-1",
            "123456789012", "123-456-789-123", 300.0, "123456789012",
            "IT", 0, 50000, 1000, 1000, 1000, 25000
        );

        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        Map<EmployeeMonthlyHoursKey, Double> workHoursMap = new HashMap<>();
        workHoursMap.put(new EmployeeMonthlyHoursKey(10001, 2024, 6), 160.0);

        YearPeriod period = new YearPeriod(2024, 6);

        List<PayrollEntry> payrollEntries = PayrollService.computeBatchPayroll(employees, workHoursMap, period);

        assertEquals(1, payrollEntries.size());
        PayrollEntry entry = payrollEntries.get(0);
        assertEquals(10001, entry.getEmployeeID());
        assertEquals("TestName TestSurname", entry.getFullName());
        assertTrue(entry.getGrossIncome() > 0);
        assertTrue(entry.getNetPay() > 0);
    }

    @Test
    public void testCalculatePayrollSummary() {
        PayrollEntry entry = new PayrollEntry(
            10001, "TestName TestSurname", "HR Rank and File",
            50000, 1125, 375, 100, 513.4, 48000
        );

        List<PayrollEntry> entries = new ArrayList<>();
        entries.add(entry);

        PayrollSummary summary = PayrollService.calculatePayrollSummary(entries);

        assertEquals(50000, summary.getTotalGrossIncome(), 0.01);
        assertEquals(1125, summary.getTotalSSS(), 0.01);
        assertEquals(375, summary.getTotalPhilhealth(), 0.01);
        assertEquals(100, summary.getTotalPagibig(), 0.01);
        assertEquals(513.4, summary.getTotalTax(), 0.01);
        assertEquals(48000, summary.getTotalNetPay(), 0.01);
    }
}
