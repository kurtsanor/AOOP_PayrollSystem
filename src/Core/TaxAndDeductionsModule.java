
package Core;


public class TaxAndDeductionsModule {
          
    public static double getSSSDeduction(double salary) {
        double deduction = 0;
        if (salary <= 0) deduction = 0;
        else if (salary < 3250) deduction =135.00;
        else if (salary < 3750) deduction = 157.50;
        else if (salary < 4250) deduction = 180.00;
        else if (salary < 4750) deduction = 202.50;
        else if (salary < 5250) deduction = 225.00;
        else if (salary < 5750) deduction = 247.50;
        else if (salary < 6250) deduction = 270.00;
        else if (salary < 6750) deduction = 292.50;
        else if (salary < 7250) deduction = 315.00;
        else if (salary < 7750) deduction = 337.50;
        else if (salary < 8250) deduction = 360.00;
        else if (salary < 8750) deduction = 382.50;
        else if (salary < 9250) deduction = 405.00;
        else if (salary < 9750) deduction = 427.50;
        else if (salary < 10250) deduction = 450.00;
        else if (salary < 10750) deduction = 472.50;
        else if (salary < 11250) deduction = 495.00;
        else if (salary < 11750) deduction = 517.50;
        else if (salary < 12250) deduction = 540.00;
        else if (salary < 12750) deduction = 562.50;
        else if (salary < 13250) deduction = 585.00;
        else if (salary < 13750) deduction = 607.50;
        else if (salary < 14250) deduction = 630.00;
        else if (salary < 14750) deduction = 652.50;
        else if (salary < 15250) deduction = 675.00;
        else if (salary < 15750) deduction = 697.50;
        else if (salary < 16250) deduction = 720.00;
        else if (salary < 16750) deduction = 742.50;
        else if (salary < 17250) deduction = 765.00;
        else if (salary < 17750) deduction = 787.50;
        else if (salary < 18250) deduction = 810.00;
        else if (salary < 18750) deduction = 832.50;
        else if (salary < 19250) deduction = 855.00;
        else if (salary < 19750) deduction = 877.50;
        else if (salary < 20250) deduction = 900.00;
        else if (salary < 20750) deduction = 922.50;
        else if (salary < 21250) deduction = 945.00;
        else if (salary < 21750) deduction = 967.50;
        else if (salary < 22250) deduction = 990.00;
        else if (salary < 22750) deduction = 1012.50;
        else if (salary < 23250) deduction = 1035.00;
        else if (salary < 23750) deduction = 1057.50;
        else if (salary < 24250) deduction = 1080.00;
        else if (salary < 24750) deduction = 1102.50;
        else deduction = 1125.00; // For salaries 24,750 and above
        
        return deduction;
    }
    
    public static double getPhilHealthDeduction (double salary) {
        double deduction;
        if (salary <= 0) deduction = 0;
        else if (salary <= 10000) deduction = 150.0;
        else if (salary <= 59999.99) deduction = (salary * 0.03) / 2;
        else deduction = 900;
        
        return Math.round(deduction * 100.0)/ 100.0;
    }
    
    public static double getPagIbigDeduction (double salary) {
        double deduction;
        if (salary < 1000) deduction = 0;
        else if (salary <= 1500) deduction = salary * 0.01;
        else deduction = Math.min(100, salary * 0.02);
        
        return Math.round(deduction * 100.0)/ 100.0;
    }
    
    public static double getWithholdingTax (double taxableSalary) {
        double deduction;
        if (taxableSalary <= 20832) deduction = 0;
        else if (taxableSalary < 33333) deduction = (taxableSalary - 20833) * 0.20;
        else if (taxableSalary < 66667) deduction = 2500 + (taxableSalary - 33333) * 0.25;
        else if (taxableSalary < 166667) deduction = 10833 + (taxableSalary - 66667) * 0.30;
        else if (taxableSalary < 666667) deduction = 40833.33 + (taxableSalary - 166667) * 0.32;
        else deduction = 200833.33 + (taxableSalary - 666667) * 0.35;
        
        return Math.round(deduction * 100.0)/ 100.0;    
    }    
}
 