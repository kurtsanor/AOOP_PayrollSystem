package Test;

import Model.*;
import Util.PdfProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PdfProcessorTest {

    private File generatedPayslipFile;
    private File generatedReportFile;

    @Test
    public void testCreatePayslipPdf_FileCreatedSuccessfully() {
        RegularEmployee emp = new RegularEmployee(
                1,
                "John",
                "Doe",
                "Developer",
                "Regular",
                LocalDate.of(1990, 1, 1),
                "123 Street",
                "09171234567",
                "SSS123",
                "PAGIBIG123",
                "TIN123",
                300, // hourlyRate
                "PHILHEALTH123",
                "User",
                0,
                48000,
                1500,
                1000,
                500,
                24000
        );

        YearPeriod period = new YearPeriod(2024, 6);
        Payslip payslip = new Payslip(
                emp,
                period,
                160,      // work hours
                48000,    // basic salary
                1000,     // sss deduction
                500,      // philhealth deduction
                1500,     // tax deduction
                300,      // pagibig deduction
                50300,    // gross pay
                47000,    // net pay
                emp.getRiceSubsidy(),
                emp.getPhoneAllowance(),
                emp.getClothingAllowance()
        );

        generatedPayslipFile = PdfProcessor.createPayslipPdf(payslip);

        assertNotNull(generatedPayslipFile);
        assertTrue(generatedPayslipFile.exists());
        assertTrue(generatedPayslipFile.length() > 0);
    }

    @Test
    public void testCreatePayrollReportPdf_FileCreatedSuccessfully() {
        RegularEmployee emp = new RegularEmployee(
                2,
                "Jane",
                "Smith",
                "Analyst",
                "Regular",
                LocalDate.of(1992, 2, 2),
                "456 Avenue",
                "09987654321",
                "SSS456",
                "PAGIBIG456",
                "TIN456",
                250, // hourlyRate
                "PHILHEALTH456",
                "User",
                0,
                40000,
                1000,
                800,
                400,
                20000
        );

        YearPeriod period = new YearPeriod(2024, 6);
        PayrollEntry entry = new PayrollEntry(
                emp.getID(),
                emp.getFirstName() + " " + emp.getLastName(),
                emp.getPosition(),
                42000,  // gross
                1000,
                500,
                300,
                1000,
                39200
        );

        List<PayrollEntry> entries = new ArrayList<>();
        entries.add(entry);

        PayrollSummary summary = new PayrollSummary(42000, 1000, 500, 300, 1000, 39200);
        generatedReportFile = PdfProcessor.createPayrollReportPdf(entries, period, summary);

        assertNotNull(generatedReportFile);
        assertTrue(generatedReportFile.exists());
        assertTrue(generatedReportFile.length() > 0);
    }

    @AfterEach
    public void cleanupGeneratedFiles() {
        if (generatedPayslipFile != null && generatedPayslipFile.exists()) {
            generatedPayslipFile.delete();
        }
        if (generatedReportFile != null && generatedReportFile.exists()) {
            generatedReportFile.delete();
        }
    }
}
