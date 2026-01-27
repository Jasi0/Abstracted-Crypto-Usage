package etl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// Minimal encryption service used by migration example
public class EtlEncryptionService {
    private final String key;

    public EtlEncryptionService(String key) {
        this.key = key;
    }

    // Encrypts text using AES/GCM/NoPadding with a deterministic IV
    /**
      @param plaintext
      @param salt
      @return
     */
    public String encrypt(String plaintext, String salt) {
        try {
            byte[] keyBytes = sha256(this.key);
            byte[] aesKey = new byte[16];
            System.arraycopy(keyBytes, 0, aesKey, 0, aesKey.length);

            // Uses deterministic IV derived from salt 
            byte[] ivFull = sha256(salt);
            byte[] iv = new byte[12];
            System.arraycopy(ivFull, 0, iv, 0, iv.length);

            SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmSpec);
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(ciphertext);
        } catch (Exception e) {
            throw new IllegalStateException("Encryption failed", e);
        }
    }

    private static byte[] sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(s.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}