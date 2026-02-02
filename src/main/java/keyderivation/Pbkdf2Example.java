package keyderivation;

import java.security.GeneralSecurityException;
import java.util.Objects;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import random.SecureRandomExample;


// Demonstrates PBKDF2 key derivation using HMAC-SHA-256
 
public class Pbkdf2Example {

    public static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final int DEFAULT_ITERATIONS = 150000;
    public static final int DEFAULT_KEY_LENGTH_BITS = 256; 
    public static final int DEFAULT_SALT_LENGTH_BYTES = 16;

    
    // Derives an AES SecretKey from a password and salt
    /**  
       @param password 
       @param salt 
       @return 
     */
    public static SecretKey deriveAesKey(char[] password, byte[] salt) {
        return deriveAesKey(password, salt, DEFAULT_ITERATIONS, DEFAULT_KEY_LENGTH_BITS);
    }

   
     // Derives an AES SecretKey from a password and salt 
     /**
       @param password 
       @param salt 
       @param iterations 
       @param keyLengthBits 
       @return 
    */
    public static SecretKey deriveAesKey(char[] password, byte[] salt, int iterations, int keyLengthBits) {
        Objects.requireNonNull(password, "password must not be null");
        Objects.requireNonNull(salt, "salt must not be null");
        if (salt.length == 0) {
            throw new IllegalArgumentException("salt must not be empty");
        }
        if (iterations <= 0) {
            throw new IllegalArgumentException("iterations must be > 0");
        }
        if (keyLengthBits <= 0) {
            throw new IllegalArgumentException("keyLengthBits must be > 0");
        }

        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] keyBytes = skf.generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 key derivation failed", e);
        }
    }

    
    // Generates a new random salt using SecureRandomExample 
    /**
       @return 
    */
    public static byte[] newSalt() {
        return SecureRandomExample.generateRandomBytes(DEFAULT_SALT_LENGTH_BYTES);
    }
}
