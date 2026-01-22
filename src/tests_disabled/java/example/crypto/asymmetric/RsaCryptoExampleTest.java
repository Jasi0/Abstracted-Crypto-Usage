package example.crypto.asymmetric;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

class RsaCryptoExampleTest {

    @Test
    void encryptDecrypt_oaep_roundtripOk() {
        KeyPair kp = RsaCryptoExample.generateKeyPair(2048);
        byte[] plaintext = "small secret".getBytes(StandardCharsets.UTF_8);

        byte[] ciphertext = RsaCryptoExample.encrypt(plaintext, kp.getPublic());
        assertNotNull(ciphertext);
        assertNotEquals(0, ciphertext.length);

        byte[] decrypted = RsaCryptoExample.decrypt(ciphertext, kp.getPrivate());
        assertArrayEquals(plaintext, decrypted);
    }

    @Test
    void decrypt_withWrongKey_fails() {
        KeyPair kp1 = RsaCryptoExample.generateKeyPair(2048);
        KeyPair kp2 = RsaCryptoExample.generateKeyPair(2048);

        byte[] plaintext = "another secret".getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext = RsaCryptoExample.encrypt(plaintext, kp1.getPublic());

        assertThrows(IllegalStateException.class, () ->
                RsaCryptoExample.decrypt(ciphertext, kp2.getPrivate()));
    }
}
