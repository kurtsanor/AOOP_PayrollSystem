/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import Domains.LeaveBalance;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author keith
 */
public class LeaveCreditsDAO {
    private final Connection connection;
    
    public LeaveCreditsDAO(Connection connection) {
        this.connection = connection;
    }
    
    public LeaveBalance getLeaveCreditsByEmpID (int employeeID) {
        String query = "SELECT * FROM leavecredits WHERE employeeID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, employeeID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new LeaveBalance(rs.getInt("employeeID"), rs.getInt("vacationLeaveCredits"), rs.getInt("medicalLeaveCredits"), rs.getInt("personalLeaveCredits"));
            }
                      
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve vacation leave credits", e);
        }
        return null;
    }
    
    public boolean updateLeaveCreditsByEmpID (int employeeID, LeaveBalance newBalance) {
        String query = "UPDATE leavecredits SET vacationLeaveCredits = ?, medicalLeaveCredits = ?, personalLeaveCredits = ? WHERE employeeID = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, newBalance.getVacationLeaveCredits());
            pst.setInt(2, newBalance.getMedicalLeaveCredits());
            pst.setInt(3, newBalance.getPersonalLeaveCredits());
            pst.setInt(4, employeeID);
            return pst.executeUpdate() > 0;
                                 
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve vacation leave credits", e);
        }
    }
    
    
    
    
    
}
