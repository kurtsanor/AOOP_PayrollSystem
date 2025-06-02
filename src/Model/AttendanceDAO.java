package Model;

import DatabaseConnection.DatabaseConnection;
import Domains.AttendanceRecord;
import Domains.YearPeriod;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public class AttendanceDAO {
      
    public AttendanceDAO () {}
    
    public List <AttendanceRecord> getAttendanceByIdAndPeriod(int employeeID, YearPeriod period) throws SQLException {
        List <AttendanceRecord> records = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL attendanceGetByIdAndMonth(?,?,?)}")) {                     
            stmt.setInt(1, employeeID);
            stmt.setInt(2, period.getYear());
            stmt.setInt(3, period.getMonth());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime timeIn = rs.getTime("timeIn").toLocalTime();
                LocalTime timeOut = (rs.getTime("timeOut") != null) ? rs.getTime("timeOut").toLocalTime() : null;              
                records.add(new AttendanceRecord(employeeID, date, timeIn, timeOut));                            
            }                               
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch attendance records", e);
        }
        
        return records;
    }
    
    // for custom date ranges
    public List<AttendanceRecord> getAttendanceByCustomRange (int employeeID, LocalDate startDate, LocalDate endDate) throws SQLException {
        List <AttendanceRecord> records = new ArrayList<>();
        
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL attendanceGetByCustomRange(?,?,?)}")) {                     
            stmt.setInt(1, employeeID);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime timeIn = rs.getTime("timeIn").toLocalTime();
                LocalTime timeOut = (rs.getTime("timeOut") != null) ? rs.getTime("timeOut").toLocalTime() : null;              
                records.add(new AttendanceRecord(employeeID, date, timeIn, timeOut));                            
            }                               
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch attendance records", e);
        }
        
        return records;
    }
    
    public boolean hasUnclosedAttendanceEntry (int employeeID, LocalDate date) throws SQLException {
      
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL attendanceHasUnclosedEntry(?,?)}")) {
            stmt.setInt(1, employeeID);
            stmt.setDate(2, Date.valueOf(date));
            return stmt.executeQuery().next();         
        } catch (SQLException e) {
            throw new SQLException("Failed to check attendance entry", e);
        }
    }
    
    public boolean saveTimeIn (int employeeID, LocalDateTime timeIn) throws SQLException {  
        
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement pst = connection.prepareCall("{CALL attendanceSaveTimeIn(?,?,?)}")) {                                                          
            pst.setInt(1, employeeID);
            pst.setDate(2, Date.valueOf(timeIn.toLocalDate()));
            pst.setTime(3, Time.valueOf(timeIn.toLocalTime()));
            return pst.executeUpdate() > 0;    
            
        } catch (SQLException e) {
            throw new SQLException("Failed to record time in", e);
        }            
    }
    
    public boolean saveTimeOut (int employeeID, LocalDateTime timeOut) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement pst = connection.prepareCall("CALL attendanceSaveTimeOut(?,?,?)")) {
            pst.setTime(1, Time.valueOf(timeOut.toLocalTime()));
            pst.setInt(2, employeeID);
            pst.setDate(3, Date.valueOf(timeOut.toLocalDate()));           
            return pst.executeUpdate() > 0;
             
        } catch (SQLException e) {
            throw new SQLException("Failed to record time out", e);
        }
    }
    
    public List<AttendanceRecord> getAllRecords () throws SQLException {
        List<AttendanceRecord> records = new ArrayList<>();       
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL attendanceGetAll()}")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AttendanceRecord currentRecord = new AttendanceRecord(rs.getInt("employeeID"),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("timeIn").toLocalTime(),
                        rs.getTime("timeOut").toLocalTime());
                records.add(currentRecord);
            }             
        } catch (SQLException e) {
            throw new SQLException("Failed to retrive attendance records",e);
        }
        return records;
    }
    
    public boolean insertLeaveAttendance(int employeeID, LocalDate start, LocalDate end) throws SQLException {
        LocalTime timeIn = LocalTime.of(8, 0);
        LocalTime timeOut = LocalTime.of(16, 0);
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("CALL attendanceInsertLeaveAttendance(?, ?, ?, ?)")){
            
            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                stmt.setInt(1, employeeID);
                stmt.setDate(2, Date.valueOf(date));
                stmt.setTime(3, Time.valueOf(timeIn));
                stmt.setTime(4, Time.valueOf(timeOut));
                stmt.addBatch();
            }
            int result [] = stmt.executeBatch();
            
            return batchUpdateSuccessful(result);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
    
    // helper method
    private boolean batchUpdateSuccessful (int result []) {
        for (int val: result) {
            // if statement execute failed (-3), return false
            if (val == -3) {
                return false;
            }
        }
        return true;
    }
    
}
