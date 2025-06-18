/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;

import Model.AttendanceRecord;
import Model.YearPeriod;
import Dao.AttendanceDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author admin
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttendanceDAOTest {
    private static AttendanceDAO dao;

    @BeforeAll
    public static void setup() {
        dao = new AttendanceDAO();
    }

    @Test
    @Order(1)
    public void testSaveTimeIn() throws SQLException {
        int employeeId = 10001;
        LocalDateTime timeIn = LocalDateTime.of(2024, 6, 1, 8, 0);

        boolean result = dao.saveTimeIn(employeeId, timeIn);
        assertTrue(result, "Time-in should be saved successfully");
    }

    @Test
    @Order(2)
    public void testHasUnclosedAttendanceEntry() throws SQLException {
        int employeeId = 10001;
        LocalDate date = LocalDate.of(2024, 6, 1);

        boolean hasUnclosed = dao.hasUnclosedAttendanceEntry(employeeId, date);
        assertTrue(hasUnclosed, "There should be an unclosed attendance entry");
    }

    @Test
    @Order(3)
    public void testSaveTimeOut() throws SQLException {
        int employeeId = 10001;
        LocalDateTime timeOut = LocalDateTime.of(2024, 6, 1, 17, 0);

        boolean result = dao.saveTimeOut(employeeId, timeOut);
        assertTrue(result, "Time-out should be saved successfully");
    }

    @Test
    @Order(4)
    public void testGetAttendanceByIdAndPeriod() throws SQLException {
        int employeeId = 10001;
        YearPeriod period = new YearPeriod(2024, 6);

        List<AttendanceRecord> records = dao.getAttendanceByIdAndPeriod(employeeId, period);
        assertNotNull(records);
        assertFalse(records.isEmpty(), "Attendance records should not be empty");
    }

    @Test
    @Order(5)
    public void testGetAttendanceByCustomRange() throws SQLException {
        int employeeId = 10001;
        LocalDate start = LocalDate.of(2024, 6, 1);
        LocalDate end = LocalDate.of(2024, 6, 30);

        List<AttendanceRecord> records = dao.getAttendanceByCustomRange(employeeId, start, end);
        assertNotNull(records);
        assertFalse(records.isEmpty(), "Custom range attendance should return records");
    }

    @Test
    @Order(6)
    public void testInsertLeaveAttendance() throws SQLException {
        int employeeId = 10001;
        LocalDate start = LocalDate.of(2024, 6, 10);
        LocalDate end = LocalDate.of(2024, 6, 12);

        boolean result = dao.insertLeaveAttendance(employeeId, start, end);
        assertTrue(result, "Leave attendance should be inserted successfully");
    }

    @Test
    @Order(7)
    public void testGetAllRecords() throws SQLException {
        List<AttendanceRecord> records = dao.getAllRecords();
        assertNotNull(records, "All records list should not be null");
        assertTrue(!records.isEmpty(), "There should be at least one attendance record");
    }
}
