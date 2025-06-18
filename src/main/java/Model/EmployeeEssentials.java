/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author keith
 */
public interface EmployeeEssentials {
    boolean requestForLeave (LeaveRequest request);
    List<AttendanceRecord> viewPersonalAttendance(int employeeID, YearPeriod period);
    List<LeaveRequest> viewPersonalLeaves(int employeeID);
    Payslip viewPersonalSalary(YearPeriod period);
    boolean timeIn(int employeeID, LocalDateTime dateTime);
    boolean timeOut(int employeeID, LocalDateTime dateTime);
}
