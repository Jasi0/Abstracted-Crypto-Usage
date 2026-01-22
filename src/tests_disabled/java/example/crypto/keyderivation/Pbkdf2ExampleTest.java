package example.crypto.keyderivation;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Pbkdf2ExampleTest {

    @Test
    void deriveKey_sameParams_sameOutput() {
        char[] password = "correct horse battery staple".toCharArray();
        byte[] salt = new byte[] {
                (byte)0x00,(byte)0x11,(byte)0x22,(byte)0x33,
                (byte)0x44,(byte)0x55,(byte)0x66,(byte)0x77,
                (byte)0x88,(byte)0x99,(byte)0xaa,(byte)0xbb,
                (byte)0xcc,(byte)0xdd,(byte)0xee,(byte)0xff
        };
        int iterations = 150_000;
        int keyLenBits = 256;

        SecretKey k1 = Pbkdf2Example.deriveAesKey(password, salt, iterations, keyLenBits);
        SecretKey k2 = Pbkdf2Example.deriveAesKey(password, salt, iterations, keyLenBits);

        assertNotNull(k1);
        assertNotNull(k2);
        assertArrayEquals(k1.getEncoded(), k2.getEncoded(), "Same inputs must yield same derived key");
        assertEquals("AES", k1.getAlgorithm());
        assertEquals(32, k1.getEncoded().length, "256-bit AES key should be 32 bytes");
    }

    @Test
    void deriveKey_differentSalt_differentOutput() {
        char[] password = "p@ssw0rd".toCharArray();
        byte[] salt1 = Pbkdf2Example.newSalt();
        byte[] salt2 = Pbkdf2Example.newSalt();

        // Ensure salts differ (statistically likely); if equal regenerate
        if (Arrays.equals(salt1, salt2)) {
            salt2 = Pbkdf2Example.newSalt();
        }

        SecretKey k1 = Pbkdf2Example.deriveAesKey(password, salt1);
        SecretKey k2 = Pbkdf2Example.deriveAesKey(password, salt2);

        assertNotNull(k1);
        assertNotNull(k2);
        assertFalse(Arrays.equals(k1.getEncoded(), k2.getEncoded()), "Different salts should produce different keys");
    }

    @Test
    void deriveKey_defaultParams_returnsAesKeyWithExpectedLength() {
        char[] password = "demo-password".toCharArray();
        byte[] salt = Pbkdf2Example.newSalt();

        SecretKey key = Pbkdf2Example.deriveAesKey(password, salt);

        assertNotNull(key);
        assertEquals("AES", key.getAlgorithm());
        assertEquals(32, key.getEncoded().length, "Default 256-bit key should be 32 bytes");
    }

    @Test
    void deriveKey_invalidParams_throwIllegalArgument() {
        char[] password = "x".toCharArray();
        byte[] salt = new byte[] { 0x01 };

        // iterations <= 0
        assertThrows(IllegalArgumentException.class, () ->
                Pbkdf2Example.deriveAesKey(password, salt, 0, 256));

        // keyLengthBits <= 0
        assertThrows(IllegalArgumentException.class, () ->
                Pbkdf2Example.deriveAesKey(password, salt, 1, 0));

        // empty salt
        assertThrows(IllegalArgumentException.class, () ->
                Pbkdf2Example.deriveAesKey(password, new byte[0], 1, 256));

        // null parameters
        assertThrows(NullPointerException.class, () ->
                Pbkdf2Example.deriveAesKey(null, salt, 1, 256));
        assertThrows(NullPointerException.class, () ->
                Pbkdf2Example.deriveAesKey(password, null, 1, 256));
    }
}
