/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Domains.AttendanceRecord;
import Domains.LeaveRequest;
import Domains.Payslip;
import Domains.YearPeriod;
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
}
