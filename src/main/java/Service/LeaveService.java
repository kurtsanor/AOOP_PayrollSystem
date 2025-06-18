/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Dao.LeaveCreditsDAO;
import Model.LeaveBalance;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author keith
 */
public class LeaveService {
        
    public boolean updateLeaveCredits (int employeeID, String leaveType, int leaveDuration) {
        try {
            LeaveCreditsDAO dao = new LeaveCreditsDAO();
            LeaveBalance leaveBalance = fetchLeaveCreditsByEmployeeID(employeeID);
            leaveBalance = deductBalanceByLeaveType(leaveType, leaveBalance, leaveDuration);
            
            return dao.updateLeaveCreditsByEmpID(employeeID, leaveBalance);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public LeaveBalance fetchLeaveCreditsByEmployeeID (int employeeID) {
        try {
            LeaveCreditsDAO dao = new LeaveCreditsDAO();
            return dao.getLeaveCreditsByEmpID(employeeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public LeaveBalance deductBalanceByLeaveType (String leaveType, LeaveBalance leaveBalance, int leaveDuration) {
        switch (leaveType) {
            case "Vacation" -> leaveBalance = leaveBalance.deductVacationCredits(leaveDuration);
            case "Medical" -> leaveBalance = leaveBalance.deductMedicalCredits(leaveDuration);
            case "Personal" -> leaveBalance = leaveBalance.deductPersonalCredits(leaveDuration);
        }
        return leaveBalance;
    }
    
    public boolean hasEnoughCredits (int employeeID, int leaveDuration, String leaveType) {
        LeaveBalance leaveBalance = fetchLeaveCreditsByEmployeeID(employeeID);
        
        switch (leaveType) {
            case "Vacation":
                return leaveBalance.getVacationLeaveCredits() >= leaveDuration;
            case "Medical":
                return leaveBalance.getMedicalLeaveCredits() >= leaveDuration;
            case "Personal":
                return leaveBalance.getPersonalLeaveCredits() >= leaveDuration;
        }      
        throw new IllegalArgumentException("Invalid leave type");
    }
    
    public static int getLeaveDuration (LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
