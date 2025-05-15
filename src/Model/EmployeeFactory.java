/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author keith
 */
public class EmployeeFactory {
    public static  Employee createEmployeeFromResultSet (ResultSet rs) throws SQLException {
        String role = rs.getString("roleName");
        Employee employee;
        
        switch (role.toUpperCase()) {
            case "HR":
                employee = new HR();
                break;
            case "FINANCE":
                employee = new Finance();
                break;
            case "IT":
                employee = new IT();
                break;
            default:
                employee = new RegularEmployee();
                break;               
        }
        employee.loadInformationFromDatabase(rs);
        return employee;
    }
}