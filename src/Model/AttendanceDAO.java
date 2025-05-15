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

public class AttendanceDAO {
      
    public AttendanceDAO () {}
    
    // Fetch all attendance records that match the given month and year.
    public List <AttendanceRecord> getAttendanceByIdAndPeriod(int employeeID, YearPeriod period) throws SQLException {
        List <AttendanceRecord> records = new ArrayList<>();
        String query = "SELECT * FROM attendance WHERE employeeID = ? AND YEAR(date) = ? AND MONTH(date) = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {                     
            pst.setInt(1, employeeID);
            pst.setInt(2, period.getYear());
            pst.setInt(3, period.getMonth());
            ResultSet rs = pst.executeQuery();
            
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
    
    // Fetch attendance records between a custom date range specified by the user.
    public List<AttendanceRecord> getAttendanceByIdAndPeriod (int employeeID, LocalDate startDate, LocalDate endDate) throws SQLException {
        List <AttendanceRecord> records = new ArrayList<>();
        String query = "SELECT * FROM attendance WHERE employeeID = ? AND date BETWEEN ? AND ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {                     
            pst.setInt(1, employeeID);
            pst.setDate(2, Date.valueOf(startDate));
            pst.setDate(3, Date.valueOf(endDate));
            ResultSet rs = pst.executeQuery();
            
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
        String query = "SELECT 1 FROM attendance WHERE employeeID = ? AND date = ? AND timeOut IS NULL";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, employeeID);
            pst.setDate(2, Date.valueOf(date));
            return pst.executeQuery().next();         
        } catch (SQLException e) {
            throw new SQLException("Failed to check attendance entry", e);
        }
    }
    
    public boolean saveTimeIn (int employeeID, LocalDateTime timeIn) throws SQLException {  
        String query = "INSERT INTO attendance (employeeID, date, timeIn) "
                     + "VALUES (?,?,?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {                                                          
            pst.setInt(1, employeeID);
            pst.setDate(2, Date.valueOf(timeIn.toLocalDate()));
            pst.setTime(3, Time.valueOf(timeIn.toLocalTime()));
            return pst.executeUpdate() > 0;    
            
        } catch (SQLException e) {
            throw new SQLException("Failed to record time in", e);
        }            
    }
    
    public boolean saveTimeOut (int employeeID, LocalDateTime timeOut) throws SQLException {
        String query = "UPDATE attendance SET timeOut = ? WHERE employeeID = ? AND date = ? AND timeOut is NULL";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
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
        String query = "SELECT * FROM attendance";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
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
       
    // gets the latest time in/time out of an employee
    public AttendanceRecord getLatestRecordByID (int employeeID) throws SQLException {
        String query = "SELECT * FROM attendance WHERE employeeID = ? ORDER BY attendanceID DESC LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
           pst.setInt(1, employeeID);
           ResultSet rs = pst.executeQuery();
           if (rs.next()) {
               return new AttendanceRecord(
                       rs.getInt("employeeID"), 
                       rs.getDate("date").toLocalDate(), 
                       rs.getTime("timeIn").toLocalTime(), 
                       rs.getTime("timeOut").toLocalTime());
           }                         
        } catch (SQLException e) {
            throw new SQLException("Failed to retrive latest attendance record",e);
        }       
        return null;
    }
    
    // method to check if an employee has an attendance record on the said date
    public boolean hasRecords (int employeeID, YearPeriod period) throws SQLException {
        String query = "SELECT 1 FROM attendance WHERE employeeID = ? AND MONTH(date) = ? AND YEAR(date) = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {            
            pst.setInt(1, employeeID);
            pst.setInt(2, period.getMonth());
            pst.setInt(3, period.getYear());
            ResultSet rs = pst.executeQuery();
            
            return rs.next();                                                
        } catch (SQLException e) {
            throw new SQLException("Failed to check a record",e);
        }
    }
    
    
}
