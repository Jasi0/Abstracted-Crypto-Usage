package signature;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;


// Demonstrates digital signatures using RSA  and ECDSA 
public class SignatureExample {

    
    // Generates an RSA KeyPair with the given key size
    public static KeyPair generateRsaKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate RSA key pair", e);
        }
    }

    
    // Generates an EC KeyPair using curve secp256r1
    public static KeyPair generateEcKeyPairP256() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(new ECGenParameterSpec("secp256r1"));
            return kpg.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate EC P-256 key pair", e);
        }
    }


    // RSA-PKCS#1 signature using SHA-256
    public static byte[] signRsaPkcs1Sha256(byte[] data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            throw new IllegalStateException("RSA PKCS#1 v1.5 signing failed", e);
        }
    }

    
    // Verifies RSA-PKCS#1 signature with SHA-256
    public static boolean verifyRsaPkcs1Sha256(byte[] data, byte[] signatureBytes, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            throw new IllegalStateException("RSA PKCS#1 v1.5 verification failed", e);
        }
    }

    
     // RSA-PSS signature using SHA-256 with MGF1(SHA-256) and salt length 32
    public static byte[] signRsaPssSha256(byte[] data, PrivateKey privateKey) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA/PSS");
            PSSParameterSpec pssSpec = new PSSParameterSpec(
                    "SHA-256",
                    "MGF1",
                    MGF1ParameterSpec.SHA256,
                    32, 
                    1   
            );
            sig.setParameter(pssSpec);
            sig.initSign(privateKey);
            sig.update(data);
            return sig.sign();
        } catch (Exception e) {
            throw new IllegalStateException("RSA-PSS signing failed", e);
        }
    }


     // Verifies RSA-PSS signature with SHA-256
    public static boolean verifyRsaPssSha256(byte[] data, byte[] signatureBytes, PublicKey publicKey) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA/PSS");
            PSSParameterSpec pssSpec = new PSSParameterSpec(
                    "SHA-256",
                    "MGF1",
                    MGF1ParameterSpec.SHA256,
                    32,
                    1
            );
            sig.setParameter(pssSpec);
            sig.initVerify(publicKey);
            sig.update(data);
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            throw new IllegalStateException("RSA-PSS verification failed", e);
        }
    }


     // ECDSA signature using SHA-256 
    public static byte[] signEcdsaSha256(byte[] data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            throw new IllegalStateException("ECDSA signing failed", e);
        }
    }


    // Verifies ECDSA signature with SHA-256 
    public static boolean verifyEcdsaSha256(byte[] data, byte[] signatureBytes, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signatureBytes);
        } catch (Exception e) {
            throw new IllegalStateException("ECDSA verification failed", e);
        }
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) {
        return signRsaPkcs1Sha256(data, privateKey);
    }

    public static boolean verify(byte[] data, byte[] signatureBytes, PublicKey publicKey) {
        return verifyRsaPkcs1Sha256(data, signatureBytes, publicKey);
    }
}
