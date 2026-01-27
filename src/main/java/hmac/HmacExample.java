package hmac;

import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


// Demonstrates HMAC operations using SHA-256
public class HmacExample {

    public static final int DEFAULT_KEY_LENGTH_BYTES = 32;

    
    // Generates a new random HMAC key 
    /**
       @param lengthBytes 
       @return 
     */
    public static byte[] newHmacKey(int lengthBytes) {
        if (lengthBytes <= 0) {
            throw new IllegalArgumentException("lengthBytes must be > 0");
        }
        byte[] key = new byte[lengthBytes];
        new SecureRandom().nextBytes(key);
        return key;
    }

    
    // Computes an HMAC tag for input data using SHA-256
    /**
       @param key 
       @param data 
       @return 
     */
    public static byte[] computeHmacSha256(byte[] key, byte[] data) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new IllegalStateException("HMAC computation failed", e);
        }
    }

    
    // Verifies an HMAC tag using SHA-256 
    /**
       @param key 
       @param data 
       @param tag 
       @return 
     */
    public static boolean verifyHmacSha256(byte[] key, byte[] data, byte[] tag) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        if (tag == null) {
            throw new IllegalArgumentException("tag must not be null");
        }
        byte[] expected = computeHmacSha256(key, data);
        return constantTimeEquals(expected, tag);
    }

    // Compares two byte arrays in constant time
    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}
