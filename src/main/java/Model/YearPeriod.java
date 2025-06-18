/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;

/**
 *
 * @author keith
 */
public class YearPeriod {
    private final LocalDate startDate;
    private final LocalDate endDate;
    
    public YearPeriod (int year, int month) {
        this.startDate = LocalDate.of(year, month, 1);
        this.endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
    }
    
    public int getYear () { return startDate.getYear(); }
    public int getMonth () { return startDate.getMonthValue(); }
}
