package Test;

import Service.LeaveRequestValidator;
import Model.LeaveBalance;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LeaveRequestValidatorTest {

    @Test
    public void testStartDateValidation_Invalid() {
        LocalDate today = LocalDate.now();
        LocalDate past = today.minusDays(1);
        LocalDate future = today.plusDays(2);

        assertEquals("This is required", LeaveRequestValidator.getStartDateValidationMessage(null, future));
        assertEquals("Date entry is outdated", LeaveRequestValidator.getStartDateValidationMessage(past, future));
        assertEquals("Must come before end date", LeaveRequestValidator.getStartDateValidationMessage(future, today));
    }

    @Test
    public void testStartDateValidation_Valid() {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(2);

        assertEquals("", LeaveRequestValidator.getStartDateValidationMessage(future, future.plusDays(1)));
    }

    @Test
    public void testEndDateValidation_Invalid() {
        LocalDate today = LocalDate.now();
        LocalDate past = today.minusDays(1);
        LocalDate future = today.plusDays(2);

        assertEquals("This is required", LeaveRequestValidator.getEndDateValidationMessage(future, null));
        assertEquals("Date entry is outdated", LeaveRequestValidator.getEndDateValidationMessage(future, past));
        assertEquals("Must come after start date", LeaveRequestValidator.getEndDateValidationMessage(future.plusDays(1), future));
    }

    @Test
    public void testEndDateValidation_Valid() {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(3);

        assertEquals("", LeaveRequestValidator.getEndDateValidationMessage(today.plusDays(1), future));
    }

    @Test
    public void testRemarksValidation_Invalid() {
        assertEquals("This is required", LeaveRequestValidator.getRemarksValidationMessage(""));
        assertEquals("This is required", LeaveRequestValidator.getRemarksValidationMessage("   "));
        assertEquals("This is required", LeaveRequestValidator.getRemarksValidationMessage(null));
    }

    @Test
    public void testRemarksValidation_Valid() {
        assertEquals("", LeaveRequestValidator.getRemarksValidationMessage("Family emergency"));
    }

    @Test
    public void testLeaveTypeValidation_Valid() {
        LeaveBalance balance = new LeaveBalance(10001, 5, 5, 5);
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(1);

        assertEquals("", LeaveRequestValidator.getLeaveTypeValidationMessage(balance, "Vacation", start, end));
        assertEquals("", LeaveRequestValidator.getLeaveTypeValidationMessage(balance, "Medical", start, end));
        assertEquals("", LeaveRequestValidator.getLeaveTypeValidationMessage(balance, "Personal", start, end));
    }

    @Test
    public void testLeaveTypeValidation_Invalid() {
        LeaveBalance balance = new LeaveBalance(10001, 1, 1, 1);
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(3);

        assertEquals("Insufficient vacation leave credits", LeaveRequestValidator.getLeaveTypeValidationMessage(balance, "Vacation", start, end));
        assertEquals("Insufficient medical leave credits", LeaveRequestValidator.getLeaveTypeValidationMessage(balance, "Medical", start, end));
        assertEquals("Insufficient personal leave credits", LeaveRequestValidator.getLeaveTypeValidationMessage(balance, "Personal", start, end));
    }
}
