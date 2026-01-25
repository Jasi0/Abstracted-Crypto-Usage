package example.crypto.random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecureRandomExampleTest {

    @Test
    void generateRandomBytes_returnsCorrectLengthAndDiffers() {
        int len = 32;
        byte[] a = SecureRandomExample.generateRandomBytes(len);
        byte[] b = SecureRandomExample.generateRandomBytes(len);

        assertNotNull(a);
        assertNotNull(b);
        assertEquals(len, a.length);
        assertEquals(len, b.length);
        // With high probability, two calls should yield different arrays
        assertFalse(java.util.Arrays.equals(a, b));
    }

    @Test
    void generateSalt_returns16Bytes() {
        byte[] salt = SecureRandomExample.generateSalt();
        assertNotNull(salt);
        assertEquals(16, salt.length);
    }
}
