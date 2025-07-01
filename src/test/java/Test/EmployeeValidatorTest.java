package Test;

import Service.EmployeeValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeValidatorTest {

    @Test
    public void testValidateFirstName_Invalid() {
        assertEquals("This is required", EmployeeValidator.validateFirstNameWithMessage(""));
        assertEquals("Name cannot contain numbers", EmployeeValidator.validateFirstNameWithMessage("John1"));
    }

    @Test
    public void testValidateFirstName_Valid() {
        assertEquals("", EmployeeValidator.validateFirstNameWithMessage("John"));
    }

    @Test
    public void testValidateLastName_Invalid() {
        assertEquals("This is required", EmployeeValidator.validateLastNameWithMessage("  "));
        assertEquals("Name cannot contain numbers", EmployeeValidator.validateLastNameWithMessage("Doe123"));
    }

    @Test
    public void testValidateLastName_Valid() {
        assertEquals("", EmployeeValidator.validateLastNameWithMessage("Doe"));
    }

    @Test
    public void testValidateBirthday_Invalid() {
        assertEquals("This is required", EmployeeValidator.validateBirthdayWithMessage(null));
        assertEquals("Birthday cannot be in the future", EmployeeValidator.validateBirthdayWithMessage(LocalDate.now().plusDays(1)));
    }

    @Test
    public void testValidateBirthday_Valid() {
        assertEquals("", EmployeeValidator.validateBirthdayWithMessage(LocalDate.of(2000, 1, 1)));
    }

    @Test
    public void testValidatePhoneNumber_Invalid() {
        assertEquals("This is required", EmployeeValidator.validatePhoneNumberWithMessage("", null));
        assertEquals("Invalid format (e.g., 123-123-123)", EmployeeValidator.validatePhoneNumberWithMessage("123123123", null));
        assertEquals("Cannot contain letters", EmployeeValidator.validatePhoneNumberWithMessage("123-12A-123", null));
        assertEquals("Invalid format (e.g., 123-123-123)", EmployeeValidator.validatePhoneNumberWithMessage("123-1234-123", null));
    }

    @Test
    public void testValidatePhoneNumber_Valid() {
        assertEquals("", EmployeeValidator.validatePhoneNumberWithMessage("123-123-123", null));
    }

    @Test
    public void testValidateAddress_Invalid() {
        assertEquals("This is required", EmployeeValidator.validateAddressWithMessage(""));
    }

    @Test
    public void testValidateAddress_Valid() {
        assertEquals("", EmployeeValidator.validateAddressWithMessage("123 Main St."));
    }

    @Test
    public void testValidateAmount_Invalid() {
        assertEquals("This is required", EmployeeValidator.validateAmountWithMessage(""));
        assertEquals("Not a valid number", EmployeeValidator.validateAmountWithMessage("abc"));
        assertEquals("Cannot be negative", EmployeeValidator.validateAmountWithMessage("-100"));
    }

    @Test
    public void testValidateAmount_Valid() {
        assertEquals("", EmployeeValidator.validateAmountWithMessage("1000.50"));
    }

    @Test
    public void testValidateSssNumber_Invalid() {
        assertEquals("This is required", EmployeeValidator.validateSssNumber("", null));
        assertEquals("Invalid format (e.g., 12-1234567-1)", EmployeeValidator.validateSssNumber("1212345671", null));
        assertEquals("Cannot contain letters", EmployeeValidator.validateSssNumber("12-123A567-1", null));
    }

    @Test
    public void testValidateSssNumber_Valid() {
        assertEquals("", EmployeeValidator.validateSssNumber("12-1234567-1", null));
    }

    @Test
    public void testValidatePhilhealth_Invalid() {
        assertEquals("This is required", EmployeeValidator.validatePhilhealthWithMessage("", null));
        assertEquals("Must consist of only 12 digits", EmployeeValidator.validatePhilhealthWithMessage("12345678901", null));
        assertEquals("Must contain digits only", EmployeeValidator.validatePhilhealthWithMessage("1234567890AB", null));
    }

    @Test
    public void testValidatePhilhealth_Valid() {
        assertEquals("", EmployeeValidator.validatePhilhealthWithMessage("123456789012", null));
    }

    @Test
    public void testValidatePagibig_Invalid() {
        assertEquals("This is required", EmployeeValidator.validatePagibigWithMessage("", null));
        assertEquals("Must consist of only 12 digits", EmployeeValidator.validatePagibigWithMessage("1234567890", null));
        assertEquals("Must contain digits only", EmployeeValidator.validatePagibigWithMessage("12345678ABCD", null));
    }

    @Test
    public void testValidatePagibig_Valid() {
        assertEquals("", EmployeeValidator.validatePagibigWithMessage("123456789012",null));
    }

    @Test
    public void testValidateTin_Invalid() {
        assertEquals("This is required", EmployeeValidator.validateTinWithMessage("", null));
        assertEquals("Invalid format (e.g., 123-123-123-123)", EmployeeValidator.validateTinWithMessage("123123123123", null));
        assertEquals("Cannot contain letters", EmployeeValidator.validateTinWithMessage("123-123-A23-123", null));
    }

    @Test
    public void testValidateTin_Valid() {
        assertEquals("", EmployeeValidator.validateTinWithMessage("123-123-123-123", null));
    }
}
