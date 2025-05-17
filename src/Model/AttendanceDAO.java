package Model;

import DatabaseConnection.DatabaseConnection;
import Domains.AttendanceRecord;
import Domains.YearPeriod;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.CallableStatement;

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

    
    
}
