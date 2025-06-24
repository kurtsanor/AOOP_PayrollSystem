package Test;

import Dao.AttendanceDAO;
import Model.EmployeeMonthlyHoursKey;
import Service.AttendanceProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AttendanceProcessorTest {

    private static AttendanceProcessor processor;

    @BeforeAll
    public static void setup() {
        AttendanceDAO dao = new AttendanceDAO();
        processor = new AttendanceProcessor(dao);
    }

    @Test
    public void testMapMonthlyHoursOfEmployeesNotEmpty() {
        Map<EmployeeMonthlyHoursKey, Double> result = processor.mapMonthlyHoursOfEmployees();
        assertTrue(result != null && !result.isEmpty(), "Monthly hours map should not be empty");
    }
}
