package Test;

import Service.TwoFactorAuthService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TwoFactorAuthServiceTest {

    @Test
    public void testGenerateSecretKey() {
        String secret = TwoFactorAuthService.generateSecretKey();
        assertNotNull(secret, "Secret key should not be null");
        assertFalse(secret.isEmpty(), "Secret key should not be empty");
    }
    
  
    @Test
    public void testVerifyCode_InvalidCodeReturnsFalse() {
        String secret = TwoFactorAuthService.generateSecretKey();
        int invalidCode = 123456;
        boolean result = TwoFactorAuthService.verifyCode(secret, invalidCode);
        assertFalse(result, "Verification should fail with invalid code");
    }

    @Test
    public void testGetQRUrl_Format() {
        String companyName = "MotorPH";
        String email = "manuel@gmail.com";
        String secret = "ABC123XYZ";
        String expected = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", companyName, email, secret, companyName);

        String actual = TwoFactorAuthService.getQRUrl(companyName, email, secret);
        assertEquals(expected, actual, "QR URL should match expected format");
    }
}
