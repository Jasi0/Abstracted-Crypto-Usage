package example.crypto.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// Demonstrates basic hashing algorithms using Java MessageDigest.
public class HashExamples {

    public static byte[] hashSha256(byte[] input) {
        return hash(input, "SHA-256");
    }

    public static byte[] hashSha512(byte[] input) {
        return hash(input, "SHA-512");
    }

    private static byte[] hash(byte[] input, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hash algorithm not available: " + algorithm, e);
        }
    }
}
