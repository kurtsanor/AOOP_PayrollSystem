/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domains;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author keith
 */
public class LeaveRequest {
    private int leaveID;
    private final int employeeID;
    private final String leaveType;  
    private final LocalDate startDate;
    private final LocalDate endDate;
    private String status;
    private final LocalDateTime submittedDate;
    private LocalDateTime processedDate;
    private final String remarks;
    
    public LeaveRequest (int employeeID, String leaveType, LocalDate startDate, LocalDate endDate, String status, LocalDateTime submittedDate, String remarks) {
        this.employeeID = employeeID;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.submittedDate = submittedDate;    
        this.remarks = remarks;
    }
    
    //getters
    public int getLeaveID () {return leaveID;}
    public int getEmployeeID () {return employeeID;}
    public String getLeaveType () {return leaveType;}
    public LocalDate getStartDate () {return startDate;}
    public LocalDate getEndDate () {return endDate;}
    public String getStatus () {return status;}
    public LocalDateTime getSubmittedDate () {return submittedDate;}
    public LocalDateTime getProcessedDate () {return processedDate;}
    public String getRemarks () {return remarks;}
    
    public void setLeaveID (int leaveID) {this.leaveID = leaveID;}
    public void setProcessedDate (LocalDateTime dateTime) {this.processedDate = dateTime;}
       
}
