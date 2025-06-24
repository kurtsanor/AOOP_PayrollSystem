/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;

import Model.AttendanceRecord;
import Model.YearPeriod;
import Dao.AttendanceDAO;
import Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
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
    private static int employeeId;
    private static LocalDate date;

    @BeforeAll
    public static void setup() {
        dao = new AttendanceDAO();
        employeeId = 10001;
        date = LocalDate.of(2024, 6, 1);
    }

    @Test
    @Order(1)
    public void testSaveTimeIn() throws SQLException {
        LocalDateTime timeIn = LocalDateTime.of(2024, 6, 1, 8, 0);

        boolean result = dao.saveTimeIn(employeeId, timeIn);
        assertTrue(result, "Time-in should be saved successfully");
    }

    @Test
    @Order(2)
    public void testHasUnclosedAttendanceEntry() throws SQLException {
        boolean hasUnclosed = dao.hasUnclosedAttendanceEntry(employeeId, date);
        assertTrue(hasUnclosed, "There should be an unclosed attendance entry");
    }

    @Test
    @Order(3)
    public void testSaveTimeOut() throws SQLException {
        LocalDateTime timeOut = LocalDateTime.of(2024, 6, 1, 17, 0);

        boolean result = dao.saveTimeOut(employeeId, timeOut);
        assertTrue(result, "Time-out should be saved successfully");
    }

    @Test
    @Order(4)
    public void testGetAttendanceByIdAndPeriod() throws SQLException {
        YearPeriod period = new YearPeriod(2024, 6);

        List<AttendanceRecord> records = dao.getAttendanceByIdAndPeriod(employeeId, period);
        assertNotNull(records);
        assertFalse(records.isEmpty(), "Attendance records should not be empty");
    }

    @Test
    @Order(5)
    public void testGetAttendanceByCustomRange() throws SQLException {
        LocalDate start = LocalDate.of(2024, 6, 1);
        LocalDate end = LocalDate.of(2024, 6, 30);

        List<AttendanceRecord> records = dao.getAttendanceByCustomRange(employeeId, start, end);
        assertNotNull(records);
        assertFalse(records.isEmpty(), "Custom range attendance should return records");
    }

    @Test
    @Order(6)
    public void testInsertLeaveAttendance() throws SQLException {
        LocalDate start = LocalDate.of(2024, 6, 1);
        LocalDate end = LocalDate.of(2024, 6, 1);

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
    
    // delete all test data
    @AfterAll
    public static void cleanUp() throws SQLException {
        
        try (Connection conn = DatabaseConnection.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM attendance WHERE employeeID = ? AND date = ?")) {
            stmt.setInt(1, employeeId);
            stmt.setDate(2, Date.valueOf(date));
            stmt.executeUpdate();
        }   
    }
}
