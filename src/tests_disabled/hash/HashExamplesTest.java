package example.crypto.hash;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HashExamplesTest {

    @Test
    void hashSha256_returnsExpectedLength() {
        byte[] input = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] out = HashExamples.hashSha256(input);
        assertNotNull(out);
        assertEquals(32, out.length, "SHA-256 output must be 32 bytes");
    }

    @Test
    void hashSha512_returnsExpectedLength() {
        byte[] input = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] out = HashExamples.hashSha512(input);
        assertNotNull(out);
        assertEquals(64, out.length, "SHA-512 output must be 64 bytes");
    }

    @Test
    void hash_handlesEmptyInput() {
        byte[] input = new byte[0];
        byte[] out256 = HashExamples.hashSha256(input);
        byte[] out512 = HashExamples.hashSha512(input);
        assertNotNull(out256);
        assertNotNull(out512);
        assertEquals(32, out256.length);
        assertEquals(64, out512.length);
    }

    @Test
    void hash_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> HashExamples.hashSha256(null));
        assertThrows(NullPointerException.class, () -> HashExamples.hashSha512(null));
    }
}
