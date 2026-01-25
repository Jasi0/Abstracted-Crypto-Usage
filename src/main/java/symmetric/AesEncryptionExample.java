package symmetric;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;


// Demonstrates symmetric encryption using AES with different modes
public class AesEncryptionExample {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static SecretKey generateKey(int keySize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(keySize);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate AES key", e);
        }
    }

    
    // Encrypts data using AES/GCM/NoPadding
    public static byte[] encryptGcm(byte[] plaintext, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            return cipher.doFinal(plaintext);
        } catch (Exception e) {
            throw new IllegalStateException("AES-GCM encryption failed", e);
        }
    }

    
    // Decrypts data using AES/GCM/NoPadding
    public static byte[] decryptGcm(byte[] ciphertext, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            return cipher.doFinal(ciphertext);
        } catch (Exception e) {
            throw new IllegalStateException("AES-GCM decryption failed", e);
        }
    }


    // Encrypts data using AES/CBC/PKCS5Padding (legacy example)
    public static byte[] encryptCbc(byte[] plaintext, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(plaintext);
        } catch (Exception e) {
            throw new IllegalStateException("AES-CBC encryption failed", e);
        }
    }

    
    // Decrypts data using AES/CBC/PKCS5Padding
    public static byte[] decryptCbc(byte[] ciphertext, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(ciphertext);
        } catch (Exception e) {
            throw new IllegalStateException("AES-CBC decryption failed", e);
        }
    }

    
    // Generates a random IV of given length
    public static byte[] generateIv(int length) {
        byte[] iv = new byte[length];
        RANDOM.nextBytes(iv);
        return iv;
    }
}
