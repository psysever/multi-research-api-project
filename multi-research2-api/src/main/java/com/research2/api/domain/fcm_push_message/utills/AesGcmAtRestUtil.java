package com.research2.api.domain.fcm_push_message.utills;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AesGcmAtRestUtil {

    private static final String PREFIX = "ENC:GCM:";
    private static final int GCM_TAG_BITS = 128;
    private final SecureRandom rnd = new SecureRandom();
    // Base64(32바이트) 키
    @Value("${aes.secret_key}")
    private String keyPlainAscii;
    private SecretKeySpec key;

    @PostConstruct
    void init() {
        byte[] raw = null;


        //Allow plaintext (ASCII) keys : Must be exactly 32 bytes
        if (raw == null && keyPlainAscii != null && !keyPlainAscii.isBlank()) {
            byte[] tmp = keyPlainAscii.getBytes(StandardCharsets.UTF_8);
            if (tmp.length == 32) raw = tmp;
        }

        if (raw == null || raw.length != 32) {
            throw new IllegalStateException("AES-GCM key must be 32 bytes (either Base64 32B via aes.atrest_key_base64,"
                    + "or 32-char ASCII via aes.secret_key)");
        }
        key = new SecretKeySpec(raw, "AES");
    }

    public String encryptToToken(String plaintext) {
        try {
            byte[] iv = new byte[12];
            rnd.nextBytes(iv);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] ct = c.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return PREFIX + Base64.getEncoder().encodeToString(iv) + ":" +
                    Base64.getEncoder().encodeToString(ct);
        } catch (Exception e) {
            throw new RuntimeException("AES-GCM Encryption Failed", e);
        }
    }

    public String decryptFromToken(String token) {
        if (token == null || !token.startsWith(PREFIX)) return token;
        try {
            String[] parts = token.split(":");
            byte[] iv = Base64.getDecoder().decode(parts[2]);
            byte[] ct = Base64.getDecoder().decode(parts[3]);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] pt = c.doFinal(ct);
            return new String(pt, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES-GCM decryption Failed", e);
        }
    }
}

