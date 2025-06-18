/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Util.DatabaseConnection;
import Model.PagibigMatrix;
import Model.PhilhealthMatrix;
import Model.SSSMatrix;
import Model.TaxMatrix;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.CallableStatement;
/**
 *
 * @author keith
 */
public class DeductionDAO {
    public List<SSSMatrix> getSSSMatrix () throws SQLException {
        List<SSSMatrix> matrix = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
            CallableStatement stmt = con.prepareCall("{CALL matrixGetSSS()}");
            ResultSet rs = stmt.executeQuery()){
            
            while (rs.next()) {
                matrix.add(new SSSMatrix(
                        rs.getDouble("minSalary"), 
                        rs.getDouble("maxSalary"), 
                        rs.getDouble("contribution")));
            }
        }
        return matrix;
    }
    
    public List<PhilhealthMatrix> getPhilhealthMatrix () throws SQLException {
        List<PhilhealthMatrix> matrix = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             CallableStatement stmt = con.prepareCall("{CALL matrixGetPhilhealth()}");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                matrix.add(new PhilhealthMatrix(
                        rs.getDouble("minSalary"), 
                        rs.getDouble("maxSalary"), 
                        rs.getDouble("premiumRate"),
                        rs.getDouble("maxContribution"),
                        rs.getDouble("employeeShareRate")));
            }
        }
        return matrix;
    }
    
    public List<PagibigMatrix> getPagibigMatrix () throws SQLException {
        List<PagibigMatrix> matrix = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
            CallableStatement stmt = con.prepareCall("{CALL matrixGetPagibig()}");
            ResultSet rs = stmt.executeQuery()){
            
            while (rs.next()) {
                matrix.add(new PagibigMatrix(
                        rs.getDouble("minSalary"), 
                        rs.getDouble("maxSalary"),
                        rs.getDouble("employeeRate"),
                        rs.getDouble("maxContribution")));
            }
        }
        return matrix;
    }
    
    public List<TaxMatrix> getTaxMatrix () throws SQLException {
        List<TaxMatrix> matrix = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
            CallableStatement stmt = con.prepareCall("{CALL matrixGetTax()}");
            ResultSet rs = stmt.executeQuery()){
            
            while (rs.next()) {
                matrix.add(new TaxMatrix(
                        rs.getDouble("minSalary"), 
                        rs.getDouble("maxSalary"), 
                        rs.getDouble("baseTax"),
                        rs.getDouble("excessRate"),
                        rs.getDouble("excessBase")));
            }
        }
        return matrix;
    }
}
