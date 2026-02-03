package tls;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


// Demonstrates TLS-related APIs using SSLContext
public class SslContextExample {

    
    // Creates an SSLContext using system default TrustManager and KeyManager
    public static SSLContext createDefaultTlsContext() {
        try {
            // Use TLSv1.3 if the provider supports it, otherwise TLS
            SSLContext ctx;
            try {
                // cryptoscan:ignore
                ctx = SSLContext.getInstance("TLSv1.3");
            } catch (Exception e) {
                ctx = SSLContext.getInstance("TLS");
            }

            // Default TrustManagerFactory with system KeyStore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore ks = KeyStore.getDefaultType() != null ? KeyStore.getInstance(KeyStore.getDefaultType()) : null;
            if (ks != null) {
                ks.load(null, null);
                tmf.init(ks);
            } else {
                tmf.init((KeyStore) null);
            }

            // cryptoscan:ignore
            // Default KeyManagerFactory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(null, null);

            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
            return ctx;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create default TLS SSLContext", e);
        }
    }


    // Creates an SSLContext with an insecure TrustManager that accepts all certificates
    public static SSLContext createInsecureTrustAllTlsContext() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            TrustManager[] trustAll = new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {  }
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {  }
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
            };
            ctx.init(null, trustAll, new SecureRandom());
            return ctx;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create insecure trust-all TLS SSLContext", e);
        }
    }

    
    // Creates an SSLSocketFactory from the given SSLContext
    public static SSLSocketFactory createSocketFactory(SSLContext context) {
        return context.getSocketFactory();
    }

    
    // Creates a HostnameVerifier that enforces HTTPS-style verification using the default JRE implementation
    public static HostnameVerifier createDefaultHostnameVerifier() {
        return HttpsURLConnection.getDefaultHostnameVerifier();
    }
}
