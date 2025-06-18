/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.time.LocalDate;

/**
 *
 * @author keith
 */
public class EmployeeValidator {
       
    private static final int PHONE_NUMBER_MAX_CHAR = 11;
    private static final int SSS_NUMBER_MAX_CHAR = 12;
    private static final int PHILHEALTH_NUMBER_MAX_CHAR = 12;
    private static final int PAGIBIG_NUMBER_MAX_CHAR = 12;
    private static final int TIN_NUMBER_MAX_CHAR = 15;
    
    public static String validateFirstNameWithMessage (String firstName) {
        if (firstName == null || firstName.trim().isBlank()) {
            return "This is required";
        }
        if (containsNumbers(firstName)) {
            return "Name cannot contain numbers";
        }
        return "";
    }
    
    public static boolean containsNumbers (String firstName) {
        for (char letter: firstName.toCharArray()) {
            if (Character.isDigit(letter)) {
                return true;
            }
        }
        return false;
    }
    
    public static String validateLastNameWithMessage (String lastName) {
        if (lastName == null || lastName.trim().isBlank()) {
            return "This is required";
        }
        if (containsNumbers(lastName)) {
            return "Name cannot contain numbers";
        }
        return "";       
    }
    
    
    
    
    public static String validateBirthdayWithMessage (LocalDate birthday) {
        if (birthday == null) {
            return "This is required";
        }
        LocalDate now = LocalDate.now();
        if (birthday.isAfter(now)) {
            return "Birthday cannot be in the future";
        }
        return "";
    }
    
    
    
    
    
    public static String validatePhoneNumberWithMessage (String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return "This is required";
        }
        
        if (phoneNumber.length() != PHONE_NUMBER_MAX_CHAR) {
            return "Invalid format (e.g., 123-123-123)";
        }
        
        int hyphenCount = 0;
        int digitCount = 0;
        for (char c: phoneNumber.toCharArray()) {
            if (c == '-') {
                hyphenCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else if (Character.isLetter(c)) {
                return "Cannot contain letters";
            }           
        }
        if (phoneNumber.charAt(3) != '-' || phoneNumber.charAt(7) != '-' || hyphenCount != 2 || digitCount != 9) {
            return "Invalid format (e.g., 123-123-123)";
        }
        return "";
    }
    
    
    
    public static String validateAddressWithMessage (String address) {
        if (address == null || address.isBlank()) {
            return "This is required";
        }
        return "";
    }
         
    public static String validateAmountWithMessage (String amount) {
        if (amount == null || amount.isBlank()) {
            return "This is required";
        }
        try {
            double convertedAmount = Double.parseDouble(amount);
            if (convertedAmount < 0) {
                return "Cannot be negative";
            }
        } catch (NumberFormatException e) {
            return "Not a valid number";
        }
        return "";
    }
    
    
    
    
    
    public static String validateSssNumber (String sssNumber) {
        if (sssNumber == null || sssNumber.isBlank()) {
            return "This is required";
        }
        
        if (sssNumber.length() != SSS_NUMBER_MAX_CHAR) {
            return "Invalid format (e.g., 12-1234567-1)";
        }
        
        int hyphenCount = 0;
        int digitCount = 0;       
        
        for (char c: sssNumber.toCharArray()) {
            if (c == '-') {
                hyphenCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else if (Character.isLetter(c)) {
                return "Cannot contain letters";
            }
        }
        if (sssNumber.charAt(2) != '-' || sssNumber.charAt(10) != '-' || hyphenCount != 2 || digitCount != 10) {
            return "Invalid format (e.g., 12-1234567-1)";
        }
        return "";
    }
    
    
    
    public static String validatePhilhealthWithMessage (String philhealthNumber) {
        if (philhealthNumber == null || philhealthNumber.isBlank()) {
            return "This is required";
        }
        
        if (philhealthNumber.length() != PHILHEALTH_NUMBER_MAX_CHAR) {
            return "Must consist of only 12 digits"; 
        }

        for (char c: philhealthNumber.toCharArray()) {
           if (!Character.isDigit(c)) {
               return "Must contain digits only";
           }
        }
        return "";
    }
    
    public static String validatePagibigWithMessage (String pagibigNumber) {
        if (pagibigNumber == null || pagibigNumber.isBlank()) {
            return "This is required";
        }
        
        if (pagibigNumber.length() != PAGIBIG_NUMBER_MAX_CHAR) {
            return "Must consist of only 12 digits"; 
        }

        for (char c: pagibigNumber.toCharArray()) {
           if (!Character.isDigit(c)) {
               return "Must contain digits only";
           }
        }
        return "";
    }
    
    
    public static String validateTinWithMessage (String tinNumber) {
        if (tinNumber == null || tinNumber.isBlank()) {
            return "This is required";
        }
        
        if (tinNumber.length() != TIN_NUMBER_MAX_CHAR) {
            return "Invalid format (e.g., 123-123-123-123)";
        }
        
        int hyphenCount = 0;
        int digitCount = 0;       
        
        for (char c: tinNumber.toCharArray()) {
            if (c == '-') {
                hyphenCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else if (Character.isLetter(c)) {
                return "Cannot contain letters";
            }
        }
        if (tinNumber.charAt(3) != '-' || tinNumber.charAt(7) != '-' || tinNumber.charAt(11) != '-' || hyphenCount != 3 || digitCount != 12) {
            return "Invalid format (e.g., 123-123-123-123)";  
        }
        return "";
    }

}