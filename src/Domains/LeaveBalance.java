/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domains;

/**
 *
 * @author keith
 */
public class LeaveBalance {
    
    private int employeeID;
    private int vacationLeaveCredits;
    private int medicalLeaveCredits;
    private int personalLeaveCredits;
    
    public LeaveBalance (int employeeID, int vacationLeaveCredits, int medicalLeaveCredits, int personalLeaveCredits) {
        this.employeeID = employeeID;
        this.vacationLeaveCredits = vacationLeaveCredits;
        this.medicalLeaveCredits = medicalLeaveCredits;
        this.personalLeaveCredits = personalLeaveCredits;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public int getVacationLeaveCredits() {
        return vacationLeaveCredits;
    }

    public int getMedicalLeaveCredits() {
        return medicalLeaveCredits;
    }

    public int getPersonalLeaveCredits() {
        return personalLeaveCredits;
    }
    
    public LeaveBalance deductVacationCredits (int days) {
        return new LeaveBalance(employeeID, vacationLeaveCredits - days, medicalLeaveCredits, personalLeaveCredits);
    }
    
    public LeaveBalance deductMedicalCredits (int days) {
        return new LeaveBalance(employeeID, vacationLeaveCredits, medicalLeaveCredits - days, personalLeaveCredits);
    }
    
    public LeaveBalance deductPersonalCredits (int days) {
        return new LeaveBalance(employeeID, vacationLeaveCredits, medicalLeaveCredits, personalLeaveCredits - days);
    }
    
    
}
