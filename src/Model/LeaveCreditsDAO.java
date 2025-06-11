/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Util.DatabaseConnection;
import Domains.LeaveBalance;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author keith
 */
public class LeaveCreditsDAO {
    
    public LeaveCreditsDAO() {}
    
    public LeaveBalance getLeaveCreditsByEmpID (int employeeID) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL leavecreditGetByID(?)}")) {
            stmt.setInt(1, employeeID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new LeaveBalance(rs.getInt("employeeID"), rs.getInt("vacationCredits"), rs.getInt("medicalCredits"), rs.getInt("personalCredits"));
                }
            }
                                        
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve vacation leave credits", e);
        }
        return null;
    }
    
    public boolean updateLeaveCreditsByEmpID (int employeeID, LeaveBalance newBalance) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement stmt = connection.prepareCall("{CALL leavecreditUpdatebyID(?,?,?)}")) {
            stmt.setInt(1, employeeID);
            stmt.setInt(2, 1);
            stmt.setInt(3, newBalance.getVacationLeaveCredits());
            stmt.addBatch();
            
            stmt.setInt(1, employeeID);
            stmt.setInt(2, 2);
            stmt.setInt(3, newBalance.getMedicalLeaveCredits());
            stmt.addBatch();
            
            stmt.setInt(1, employeeID);
            stmt.setInt(2, 3);
            stmt.setInt(3, newBalance.getPersonalLeaveCredits());
            stmt.addBatch();
            
            int [] result = stmt.executeBatch();
            
            return batchUpdateSuccessful(result);
                                 
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve vacation leave credits", e);
        }
    }
    
    // helper method
    private boolean batchUpdateSuccessful (int result []) {
        for (int val: result) {
            // if statement execute failed (-3), return false
            if (val == -3) {
                return false;
            }
        }
        return true;
    }
    
    public List<LeaveBalance> getAllEmployeeLeaveCredits() throws SQLException {
        List<LeaveBalance> balances = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
            CallableStatement stmt = connection.prepareCall("CALL leavecreditGetAll()");
            ResultSet rs = stmt.executeQuery()){
            
            while (rs.next()) {
                balances.add(new LeaveBalance(rs.getInt("employeeID"), 
                        rs.getInt("vacationCredits"), 
                        rs.getInt("medicalCredits"), 
                        rs.getInt("personalCredits")));
            }
            
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return balances;
    }
    
    
    
    
    
}
