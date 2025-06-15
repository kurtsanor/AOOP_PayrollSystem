/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

/**
 *
 * @author admin
 */
public class TwoFactorAuthService {
    private final static GoogleAuthenticator auth;
    
    static {
        auth = new GoogleAuthenticator();
    }
    
    public static String generateSecretKey() {       
        GoogleAuthenticatorKey key = auth.createCredentials();
        return key.getKey();
    }
    
    public static boolean verifyCode(String secret, int code) {
        return auth.authorize(secret, code);
    }
    
    public static String getQRUrl(String companyName, String email, String secret) {
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", companyName, email, secret, companyName);
    }   
    
}
