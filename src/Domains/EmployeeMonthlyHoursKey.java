/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domains;

import java.util.Objects;

/**
 *
 * @author keith
 */
public class EmployeeMonthlyHoursKey {
    private final int employeeID;
    private final int year;
    private final int month;
    
    public EmployeeMonthlyHoursKey (int employeeID, int year, int month) {
        this.employeeID = employeeID;
        this.year = year;
        this.month = month;
        
    }
    
    public int getEmployeeID () { return employeeID; }
    public int getYear () { return year; }
    public int getMonth () { return month; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeMonthlyHoursKey)) return false;
        EmployeeMonthlyHoursKey other = (EmployeeMonthlyHoursKey) o;
        return employeeID == other.employeeID && 
               year == other.year && 
               month == other.month;
    }
    
    @Override
    public String toString() {
        return "EmployeeMonthlyHoursKey{" + "employeeID=" + employeeID + ", year=" + year + ", month=" + month + '}';
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(employeeID, year, month);
    }
}
