/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopClasses;

import Domains.AttendanceRecord;
import Domains.LeaveRequest;
import Domains.YearPeriod;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author keith
 */
public interface EmployeeEssentials {
    boolean requestForLeave (LeaveRequest request) throws SQLException, ClassNotFoundException;
    List<AttendanceRecord> viewPersonalAttendance(int employeeID, YearPeriod period) throws SQLException, ClassNotFoundException;
    List<LeaveRequest> viewPersonalLeaves(int employeeID) throws SQLException, ClassNotFoundException;
}
