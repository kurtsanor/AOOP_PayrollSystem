/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import DatabaseConnection.DatabaseConnection;
import Domains.LeaveBalance;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *
 * @author keith
 */
public class LeaveCreditsDAO {
    
    public LeaveCreditsDAO() {}
    
    public LeaveBalance getLeaveCreditsByEmpID (int employeeID) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL leavecreditsGetByID(?)}")) {
            stmt.setInt(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new LeaveBalance(rs.getInt("employeeID"), rs.getInt("vacationLeaveCredits"), rs.getInt("medicalLeaveCredits"), rs.getInt("personalLeaveCredits"));
            }
                      
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve vacation leave credits", e);
        }
        return null;
    }
    
    public boolean updateLeaveCreditsByEmpID (int employeeID, LeaveBalance newBalance) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL leavecreditsUpdatebyID(?,?,?,?)}")) {
            stmt.setInt(1, newBalance.getVacationLeaveCredits());
            stmt.setInt(2, newBalance.getMedicalLeaveCredits());
            stmt.setInt(3, newBalance.getPersonalLeaveCredits());
            stmt.setInt(4, employeeID);
            return stmt.executeUpdate() > 0;
                                 
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve vacation leave credits", e);
        }
    }
    
    
    
    
    
}
