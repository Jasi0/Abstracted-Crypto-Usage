package x509;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;


// Demonstrates X.509 certificate generation and self-signing.
public class X509Example {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    
    // Generates a self-signed X.509 certificate and a private key.
    /**
       @param commonName 
       @param keyType 
       @param daysValid 
       @return 
     */
    public static GeneratedCert generateSelfSignedCert(String commonName, String keyType, int daysValid) {
        Objects.requireNonNull(commonName, "commonName must not be null");
        Objects.requireNonNull(keyType, "keyType must not be null");
        if (commonName.isBlank()) {
            throw new IllegalArgumentException("commonName must not be empty");
        }
        if (daysValid <= 0) {
            throw new IllegalArgumentException("daysValid must be > 0");
        }

        try {
            KeyPair keyPair;
            String sigAlg;
            if ("ECDSA".equalsIgnoreCase(keyType)) {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
                kpg.initialize(new ECGenParameterSpec("secp256r1"));
                keyPair = kpg.generateKeyPair();
                sigAlg = "SHA256withECDSA";
            } else if ("RSA".equalsIgnoreCase(keyType)) {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                keyPair = kpg.generateKeyPair();
                sigAlg = "SHA256withRSA";
            } else {
                throw new IllegalArgumentException("keyType must be 'ECDSA' or 'RSA'");
            }

            X500Name subject = new X500Name("CN=" + commonName);
            BigInteger serial = new BigInteger(64, new java.security.SecureRandom());
            Date notBefore = Date.from(Instant.now().minusSeconds(60));
            Date notAfter = Date.from(Instant.now().plusSeconds(86400L * daysValid));

            SubjectPublicKeyInfo spki = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
            X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
                    subject,
                    serial,
                    notBefore,
                    notAfter,
                    subject, // self-signed => issuer = subject
                    spki
            );

            builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));

            ContentSigner signer = new JcaContentSignerBuilder(sigAlg)
                    .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                    .build(keyPair.getPrivate());

            X509CertificateHolder holder = builder.build(signer);
            X509Certificate cert = new JcaX509CertificateConverter()
                    .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                    .getCertificate(holder);

            return new GeneratedCert(cert, keyPair.getPrivate());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate self-signed certificate", e);
        }
    }

    
    // Saves a PEM-encoded certificate to disk.
    /**
       @param cert 
       @param path 
       @return 
     */
    public static void saveCertPem(X509Certificate cert, Path path) {
        Objects.requireNonNull(cert, "cert must not be null");
        Objects.requireNonNull(path, "path must not be null");
        try {
            byte[] der = cert.getEncoded();
            String pem = toPem("CERTIFICATE", der);
            try (Writer w = new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8)) {
                w.write(pem);
            }
        } catch (CertificateEncodingException | IOException e) {
            throw new IllegalStateException("Failed to save certificate PEM", e);
        }
    }

    
    // Saves a PKCS#8 PEM-encoded private key to disk (without password protection).
    /**
       @param privateKey 
       @param path 
       @return 
     */
    public static void savePrivateKeyPem(PrivateKey privateKey, Path path) {
        Objects.requireNonNull(privateKey, "privateKey must not be null");
        Objects.requireNonNull(path, "path must not be null");
        try {
            byte[] der = privateKey.getEncoded();
            String pem = toPem("PRIVATE KEY", der);
            try (Writer w = new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8)) {
                w.write(pem);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save private key PEM", e);
        }
    }

    
    // Loads a certificate from PEM.
    /**
       @param path 
       @return 
     */
    public static X509Certificate loadCertPem(Path path) {
        Objects.requireNonNull(path, "path must not be null");
        try {
            byte[] data = Files.readAllBytes(path);
            String pem = new String(data, StandardCharsets.UTF_8);
            String base64 = extractPemContent(pem, "CERTIFICATE");
            byte[] der = Base64.getMimeDecoder().decode(base64);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(new java.io.ByteArrayInputStream(der));
        } catch (IOException | CertificateException e) {
            throw new IllegalStateException("Failed to load certificate from PEM", e);
        }
    }

    
    // Verifies a self-signed certificate's signature using its public key.
    /**
       @param cert 
       @return 
     */
    public static boolean verifySelfSignature(X509Certificate cert) {
        Objects.requireNonNull(cert, "cert must not be null");
        try {
            cert.verify(cert.getPublicKey());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Utility: wraps DER bytes into a PEM block.
    private static String toPem(String type, byte[] der) {
        String base64 = Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(der);
        return "-----BEGIN " + type + "-----\n" + base64 + "\n-----END " + type + "-----\n";
        }

    // Utility: extracts the Base64 content from a PEM block.
    private static String extractPemContent(String pem, String type) {
        String begin = "-----BEGIN " + type + "-----";
        String end = "-----END " + type + "-----";
        int s = pem.indexOf(begin);
        int e = pem.indexOf(end);
        if (s < 0 || e < 0) {
            throw new IllegalArgumentException("Invalid PEM format for type: " + type);
        }
        String base64 = pem.substring(s + begin.length(), e);
        return base64.replaceAll("\\s", "");
    }

    // Holds a generated certificate and its private key.
    public static class GeneratedCert {
        public final X509Certificate certificate;
        public final PrivateKey privateKey;

        public GeneratedCert(X509Certificate certificate, PrivateKey privateKey) {
            this.certificate = certificate;
            this.privateKey = privateKey;
        }
    }
}
