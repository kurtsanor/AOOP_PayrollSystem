/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import Domains.YearPeriod;
import Model.AttendanceDAO;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import Model.HoursCalculator;
import java.time.LocalTime;
/**
 *
 * @author admin
 */
public class HoursCalculatorTest {
    
    @Test
    public void testCalculateDailyHours() {
        LocalTime timeIn = LocalTime.of(8,0);
        LocalTime timeOut = LocalTime.of(7,0);
        double actualResult = HoursCalculator.calculateDailyHours(timeIn, timeOut);
        assertEquals(23, actualResult);
    }
    
    @Test
    public void testCalculateTotalHoursByPeriod() {
        AttendanceDAO dao = new AttendanceDAO();
        YearPeriod period = new YearPeriod(2022, 7);
        double hoursWorked = HoursCalculator.calculateTotalHoursByPeriod(10001, period, dao);
        assertTrue(hoursWorked > 0, "Work hours of employee should be recorded");
    }
    
}
