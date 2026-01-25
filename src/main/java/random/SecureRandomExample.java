package random;

import java.security.SecureRandom;


//Demonstrates usage of SecureRandom for cryptographic purposes
public class SecureRandomExample {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    
    // Generates a random byte array of given length
    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
        return bytes;
    }

    
    // Generates a random salt, commonly used for hashing or key derivation
    public static byte[] generateSalt() {
        return generateRandomBytes(16);
    }
}
