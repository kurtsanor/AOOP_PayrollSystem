/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author keith
 */
public class AttendanceRecord {
    private final int employeeID;
    private final LocalDate date;
    private final LocalTime timeIn;
    private final LocalTime timeOut;
    
    public AttendanceRecord (int employeeID, LocalDate date, LocalTime timeIn, LocalTime timeOut) {
        this.employeeID = employeeID;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }
    
    public int getEmployeeID() { return employeeID; }
    public LocalDate getDate() { return date; }
    public LocalTime getTimeIn() { return timeIn; }
    public LocalTime getTimeOut() { return timeOut; }
}
