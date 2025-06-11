/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Domains.LeaveBalance;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.sql.SQLException;

/**
 *
 * @author keith
 */
public class LeaveRequestValidator {
    public static String getStartDateValidationMessage (LocalDate startDate, LocalDate endDate) {              
        LocalDate dateNow = LocalDate.now();
        if (startDate == null) {
            return "This is required";
        }
        if (startDate.isBefore(dateNow)) {
            return "Date entry is outdated";
        }
        if (endDate == null) {
            return "";
        }              
        if (startDate.isAfter(endDate)) {
            return "Must come before end date";
        }
        return "";
    }
    
    public static String getEndDateValidationMessage (LocalDate startDate, LocalDate endDate) {                     
        LocalDate dateNow = LocalDate.now();
        if (endDate == null) {
            return "This is required";
        }
        if (endDate.isBefore(dateNow)) {
            return "Date entry is outdated";
        }
        if (startDate == null) {
            return "";
        }       
        if (endDate.isBefore(startDate)) {
            return "Must come after start date";
        }
        return "";
    }
    
    public static String getRemarksValidationMessage (String remarks) {
        return remarks == null || remarks.isBlank() ? "This is required" : "";
    }
    
    public static String getLeaveTypeValidationMessage (LeaveBalance balance, String leaveType, LocalDate startDate, LocalDate endDate) {
        LocalDate dateNow = LocalDate.now();
        if (startDate == null || endDate == null || startDate.isBefore(dateNow) || endDate.isBefore(dateNow)) {
            return "";
        }
        
        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        switch (leaveType) {
            case "Vacation":
                return totalDays > balance.getVacationLeaveCredits() ? "Insufficient vacation leave credits" : "";
            case "Medical":
                return totalDays > balance.getMedicalLeaveCredits() ? "Insufficient medical leave credits" : "";
            case "Personal":
                return totalDays > balance.getPersonalLeaveCredits() ? "Insufficient personal leave credits": "";
        }
        return "";      
    }
    
    public static String getOverlapLeaveDateMessage (int employeeID, LocalDate startDate, LocalDate endDate) {
        LocalDate dateNow = LocalDate.now();
        if (startDate == null) {
            return "This is required";
        }
        if (startDate.isBefore(dateNow)) {
            return "Date entry is outdated";
        }
        if (endDate == null) {
            return "";
        }              
        if (startDate.isAfter(endDate)) {
            return "Must come before end date";
        }
        try {
            LeaveDAO dao = new LeaveDAO();
            return dao.hasOverlappingApprovedLeave(employeeID, startDate, endDate) ? "Dates overlap with an approved leave" : "";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
