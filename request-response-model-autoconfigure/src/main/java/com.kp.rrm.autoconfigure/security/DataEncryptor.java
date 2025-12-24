package com.kp.rrm.autoconfigure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DataEncryptor {

    private static final Logger log = LoggerFactory.getLogger(DataEncryptor.class);

    private final SecretKeySpec keySpec;

    public DataEncryptor(String key) {
        log.info("DataEncryptor constructor called with key length: {}", key != null ? key.length() : 0);
        if (key == null || !(key.length() == 16 || key.length() == 24 || key.length() == 32)) {
            String error = "Invalid AES key length. Key must be 16, 24, or 32 bytes. Current length: " + (key == null ? 0 : key.length());
            log.error(error);
            throw new IllegalStateException(error);
        }
        this.keySpec = new SecretKeySpec(key.getBytes(), "AES");
        log.info("DataEncryptor initialized successfully with {} byte key", key.length());
    }

    public String encrypt(String value) {
        log.debug("DataEncryptor.encrypt() called with value: {}", value != null ? "[HIDDEN]" : "NULL");
        if (value == null) {
            log.debug("Value is NULL, returning NULL");
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            String encrypted = Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
            log.info("Encryption successful. Original length: {}, Encrypted: {}", value.length(), encrypted);
            return encrypted;
        } catch (Exception e) {
            log.error("Encryption failed for value: {}", e.getMessage(), e);
            throw new RuntimeException("Encryption failed: " + e.getMessage(), e);
        }
    }
}
