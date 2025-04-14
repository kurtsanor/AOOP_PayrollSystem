/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopClasses;

import Domains.AttendanceRecord;
import Domains.YearPeriod;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 *
 * @author keith
 */
public class HoursCalculator {
    
    public static double calculateDailyHours (LocalTime timeIn, LocalTime timeOut) {
        if (timeOut == null || timeIn == null) {
            return 0.0;
        }       
        long minutes = ChronoUnit.MINUTES.between(timeIn, timeOut);
        double hours = minutes / 60.0;
        return Math.round(hours * 100.0) / 100.0;
    }
    
    public static double calculateTotalHoursByPeriod (int employeeID, YearPeriod period, AttendanceDatabase database) {
        List <AttendanceRecord> records = database.getAttendanceByIdAndPeriod(employeeID, period);
        
        return calculateTotalHours(records);
    }
    
    public static double calculateTotalHours (List<AttendanceRecord> records) {
        double totalHours = 0.0;
        
        for (AttendanceRecord record: records) {
            totalHours += calculateDailyHours(record.getTimeIn(), record.getTimeOut());
        }
        return totalHours;
    }
    
    
}
