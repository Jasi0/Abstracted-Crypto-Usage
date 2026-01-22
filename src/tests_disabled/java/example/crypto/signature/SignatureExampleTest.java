package example.crypto.signature;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

class SignatureExampleTest {

    @Test
    void rsaPss_signVerify_ok() {
        KeyPair kp = SignatureExample.generateRsaKeyPair(2048);
        byte[] data = "message to sign".getBytes(StandardCharsets.UTF_8);

        byte[] sig = SignatureExample.signRsaPssSha256(data, kp.getPrivate());
        assertNotNull(sig);
        assertTrue(SignatureExample.verifyRsaPssSha256(data, sig, kp.getPublic()));
    }

    @Test
    void ecdsa_signVerify_ok() {
        KeyPair kp = SignatureExample.generateEcKeyPairP256();
        byte[] data = "ecdsa payload".getBytes(StandardCharsets.UTF_8);

        byte[] sig = SignatureExample.signEcdsaSha256(data, kp.getPrivate());
        assertNotNull(sig);
        assertTrue(SignatureExample.verifyEcdsaSha256(data, sig, kp.getPublic()));
    }

    @Test
    void verify_failsOnManipulation() {
        // RSA-PSS negative test
        KeyPair kp1 = SignatureExample.generateRsaKeyPair(2048);
        KeyPair kp2 = SignatureExample.generateRsaKeyPair(2048);
        byte[] data = "integrity check".getBytes(StandardCharsets.UTF_8);

        byte[] sig = SignatureExample.signRsaPssSha256(data, kp1.getPrivate());

        // Wrong key should fail
        assertFalse(SignatureExample.verifyRsaPssSha256(data, sig, kp2.getPublic()));

        // Tamper with signature bytes should fail
        byte[] tampered = sig.clone();
        tampered[tampered.length - 1] ^= 0x01;
        assertFalse(SignatureExample.verifyRsaPssSha256(data, tampered, kp1.getPublic()));
    }
}
