package example.crypto.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import example.crypto.asymmetric.RsaCryptoExample;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtExampleTest {

    @Test
    void hs256_signVerify_ok() {
        byte[] secret = "super-secret-1234567890".getBytes(StandardCharsets.UTF_8);
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(60);

        String token = JwtExample.createHs256Token(
                "alice",
                "example-issuer",
                now,
                exp,
                Map.of("role", "user"),
                secret
        );

        DecodedJWT jwt = JwtExample.verifyHs256Token(token, secret, "example-issuer");
        assertNotNull(jwt);
        assertEquals("alice", jwt.getSubject());
        assertEquals("example-issuer", jwt.getIssuer());
        assertEquals("user", jwt.getClaim("role").asString());
        assertTrue(jwt.getExpiresAt().toInstant().isAfter(now));
    }

    @Test
    void rs256_signVerify_ok() {
        KeyPair kp = RsaCryptoExample.generateKeyPair(2048);
        RSAPrivateKey priv = (RSAPrivateKey) kp.getPrivate();
        RSAPublicKey pub = (RSAPublicKey) kp.getPublic();

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(60);

        String token = JwtExample.createRs256Token(
                "bob",
                "example-issuer",
                now,
                exp,
                Map.of("scope", "read"),
                priv
        );

        DecodedJWT jwt = JwtExample.verifyRs256Token(token, pub, "example-issuer");
        assertNotNull(jwt);
        assertEquals("bob", jwt.getSubject());
        assertEquals("example-issuer", jwt.getIssuer());
        assertEquals("read", jwt.getClaim("scope").asString());
    }

    @Test
    void verify_failsOnBadIssuerOrExpired() {
        byte[] secret = "another-super-secret".getBytes(StandardCharsets.UTF_8);
        Instant now = Instant.now();

        // Token with future exp but issuer "iss-A"
        String token = JwtExample.createHs256Token(
                "charlie",
                "iss-A",
                now,
                now.plusSeconds(30),
                Map.of(),
                secret
        );

        // Wrong issuer -> should fail
        assertThrows(JWTVerificationException.class, () ->
                JwtExample.verifyHs256Token(token, secret, "iss-B"));

        // Expired token -> should fail
        String expired = JwtExample.createHs256Token(
                "dave",
                "iss-C",
                now.minusSeconds(120),
                now.minusSeconds(60),
                Map.of(),
                secret
        );
        assertThrows(JWTVerificationException.class, () ->
                JwtExample.verifyHs256Token(expired, secret, "iss-C"));
    }
}
