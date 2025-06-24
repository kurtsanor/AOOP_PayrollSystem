package Test;

import Model.LeaveBalance;
import Service.LeaveService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LeaveServiceTest {

    private static LeaveService service;
    private static final int EMPLOYEE_ID = 10001;

    @BeforeAll
    public static void init() {
        service = new LeaveService();
    }

    @Test
    public void testGetLeaveDuration() {
        LocalDate start = LocalDate.of(2024, 6, 20);
        LocalDate end = LocalDate.of(2024, 6, 22);
        int duration = LeaveService.getLeaveDuration(start, end);
        assertEquals(3, duration);
    }

    @Test
    public void testDeductBalanceByLeaveType_Vacation() {
        LeaveBalance balance = new LeaveBalance(EMPLOYEE_ID, 5, 5, 5);
        LeaveBalance updated = service.deductBalanceByLeaveType("Vacation", balance, 2);
        assertEquals(3, updated.getVacationLeaveCredits());
    }

    @Test
    public void testDeductBalanceByLeaveType_Medical() {
        LeaveBalance balance = new LeaveBalance(EMPLOYEE_ID, 5, 5, 5);
        LeaveBalance updated = service.deductBalanceByLeaveType("Medical", balance, 1);
        assertEquals(4, updated.getMedicalLeaveCredits());
    }

    @Test
    public void testDeductBalanceByLeaveType_Personal() {
        LeaveBalance balance = new LeaveBalance(EMPLOYEE_ID, 5, 5, 5);
        LeaveBalance updated = service.deductBalanceByLeaveType("Personal", balance, 3);
        assertEquals(2, updated.getPersonalLeaveCredits());
    }

    @Test
    public void testFetchLeaveCreditsByEmployeeID() {
        LeaveBalance balance = service.fetchLeaveCreditsByEmployeeID(EMPLOYEE_ID);
        assertNotNull(balance);
        assertEquals(EMPLOYEE_ID, balance.getEmployeeID());
    }

    @Test
    public void testHasEnoughCredits_True() {
        boolean hasEnough = service.hasEnoughCredits(EMPLOYEE_ID, 1, "Vacation");
        assertTrue(hasEnough);
    }

    @Test
    public void testHasEnoughCredits_False() {
        boolean hasEnough = service.hasEnoughCredits(EMPLOYEE_ID, 9999, "Medical");
        assertFalse(hasEnough);
    }

}
