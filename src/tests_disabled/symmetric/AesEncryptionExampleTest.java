package example.crypto.symmetric;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AesEncryptionExampleTest {

    @Test
    void encryptDecrypt_gcm_roundtripOk() {
        SecretKey key = AesEncryptionExample.generateKey(128);
        byte[] iv = AesEncryptionExample.generateIv(12); // 12-byte IV for GCM
        byte[] plaintext = "The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.UTF_8);

        byte[] ciphertext = AesEncryptionExample.encryptGcm(plaintext, key, iv);
        byte[] decrypted = AesEncryptionExample.decryptGcm(ciphertext, key, iv);

        assertArrayEquals(plaintext, decrypted);
    }

    @Test
    void decrypt_gcm_failsOnTamper() {
        SecretKey key = AesEncryptionExample.generateKey(128);
        byte[] iv = AesEncryptionExample.generateIv(12);
        byte[] plaintext = "authenticated encryption".getBytes(StandardCharsets.UTF_8);

        byte[] ciphertext = AesEncryptionExample.encryptGcm(plaintext, key, iv);

        // Tamper with ciphertext/tag (flip last byte)
        byte[] tampered = Arrays.copyOf(ciphertext, ciphertext.length);
        tampered[tampered.length - 1] ^= 0x01;

        assertThrows(IllegalStateException.class, () ->
                AesEncryptionExample.decryptGcm(tampered, key, iv));
    }

    @Test
    void encryptDecrypt_cbc_roundtripOk() {
        SecretKey key = AesEncryptionExample.generateKey(128);
        byte[] iv = AesEncryptionExample.generateIv(16); // 16-byte IV for CBC
        byte[] plaintext = "legacy mode example".getBytes(StandardCharsets.UTF_8);

        byte[] ciphertext = AesEncryptionExample.encryptCbc(plaintext, key, iv);
        byte[] decrypted = AesEncryptionExample.decryptCbc(ciphertext, key, iv);

        assertArrayEquals(plaintext, decrypted);
    }
}
