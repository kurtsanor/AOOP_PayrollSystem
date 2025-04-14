/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopClasses;

import Domains.AttendanceRecord;
import Domains.EmployeeMonthlyHoursKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author keith
 */
public class AttendanceProcessor {
    
    private final AttendanceDatabase database;
    private final HoursCalculator hoursCalculator;
    
    public AttendanceProcessor (AttendanceDatabase database, HoursCalculator hoursCalculator) {
        this.database = database;
        this.hoursCalculator = hoursCalculator;
    }
    
    public Map<EmployeeMonthlyHoursKey, Double> calculateMonthlyHours () {
        List<AttendanceRecord> records = database.getAllRecords();
        Map<EmployeeMonthlyHoursKey, Double> workHoursMap = new HashMap<>();
        
        for (AttendanceRecord record: records) {
            EmployeeMonthlyHoursKey key = new EmployeeMonthlyHoursKey(
                    record.getEmployeeID(),
                    record.getDate().getYear(),
                    record.getDate().getMonthValue());
            
            double hours = hoursCalculator.calculateDailyHours(record.getTimeIn(), record.getTimeOut());
            
            workHoursMap.put(key, workHoursMap.getOrDefault(key, 0.0) + hours);
        }
        return workHoursMap;
    }
    
    
}
