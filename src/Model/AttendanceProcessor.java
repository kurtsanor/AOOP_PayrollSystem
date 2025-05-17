/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Domains.AttendanceRecord;
import Domains.EmployeeMonthlyHoursKey;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author keith
 */
public class AttendanceProcessor {
    
    private final AttendanceDAO database;
    
    public AttendanceProcessor (AttendanceDAO database) {
        this.database = database;
    }
    
    public Map<EmployeeMonthlyHoursKey, Double> mapMonthlyHoursOfEmployees () {
        try {
            List<AttendanceRecord> records = database.getAllRecords();
            Map<EmployeeMonthlyHoursKey, Double> workHoursMap = new HashMap<>();
            
            for (AttendanceRecord record: records) {
                EmployeeMonthlyHoursKey key = new EmployeeMonthlyHoursKey(
                        record.getEmployeeID(),
                        record.getDate().getYear(),
                        record.getDate().getMonthValue());
                
                double hours = HoursCalculator.calculateDailyHours(record.getTimeIn(), record.getTimeOut());
                
                workHoursMap.put(key, workHoursMap.getOrDefault(key, 0.0) + hours);
            }
            return workHoursMap;
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
}
