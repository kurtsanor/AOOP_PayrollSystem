/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Dao.AttendanceDAO;
import Model.AttendanceRecord;
import Model.YearPeriod;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author keith
 */
public class HoursCalculator {
    
    public static double calculateDailyHours (LocalTime timeIn, LocalTime timeOut) {
        if (timeIn == null || timeOut == null) {
            return 0.0;
        }
        
        if (timeIn.equals(timeOut)) {
            return 0.0;
        }
              
        long minutesWorked;

        if (timeOut.isAfter(timeIn)) {
            // Same day
            minutesWorked = ChronoUnit.MINUTES.between(timeIn, timeOut);
        } else {
            // Overnight shift:
            long minutesUntilMidnight = ChronoUnit.MINUTES.between(timeIn, LocalTime.MAX) + 1;
            long minutesAfterMidnight = ChronoUnit.MINUTES.between(LocalTime.MIN, timeOut);    
            minutesWorked = minutesUntilMidnight + minutesAfterMidnight;
        }

        double hours = minutesWorked / 60.0;
        return Math.round(hours * 100.0) / 100.0;
    }
    
    public static double calculateTotalHoursByPeriod (int employeeID, YearPeriod period, AttendanceDAO database) {
        try {
            List <AttendanceRecord> records = database.getAttendanceByIdAndPeriod(employeeID, period);
            
            return calculateTotalHours(records);
        } catch (SQLException ex) {
            Logger.getLogger(HoursCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public static double calculateTotalHours (List<AttendanceRecord> records) {
        double totalHours = 0.0;
        
        for (AttendanceRecord record: records) {
            totalHours += calculateDailyHours(record.getTimeIn(), record.getTimeOut());
        }
        return totalHours;
    }
    
    public static int getHours (double value) {
        return (int) value;      
    }
    
    public static int getMinutes (double value) {
        return (int) Math.round((value * 60) % 60);
    }
    
    
}
