package com.research2.api.domain.fcm_push_message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RSAKeyProvider {

    @Value("${crypto.rsa.public-key-pem-path}")
    private Resource publicPem;

    @Value("${crypto.rsa.private-key-pem-path}")
    private Resource privatePem;

    public RSAPublicKey getPublicKey() {
        try (InputStream in = publicPem.getInputStream()) {
            String pem = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            String base64 = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] keyBytes = Base64.getDecoder().decode(base64);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Public key load failed", e);
        }
    }

    public RSAPrivateKey getPrivateKey() {
        try (InputStream in = privatePem.getInputStream()) {
            String pem = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            String base64 = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] keyBytes = Base64.getDecoder().decode(base64);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Private key load failed", e);
        }
    }
}
