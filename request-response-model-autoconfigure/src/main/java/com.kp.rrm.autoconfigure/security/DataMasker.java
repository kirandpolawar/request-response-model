package com.kp.rrm.autoconfigure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataMasker {
    
    private static final Logger log = LoggerFactory.getLogger(DataMasker.class);
    
    public static String maskEmail(String email) {
        log.debug("DataMasker.maskEmail() called with: {}", email);
        if (email == null || email.isEmpty()) {
            log.debug("Email is null or empty, returning as-is");
            return email;
        }
        
        if (!email.contains("@")) {
            log.debug("Not an email format, using general string masking");
            return maskString(email);
        }
        
        String[] parts = email.split("@");
        if (parts.length != 2) {
            log.debug("Invalid email format, using general string masking");
            return maskString(email);
        }
        
        String localPart = parts[0];
        String domain = parts[1];
        
        if (localPart.length() <= 1) {
            log.debug("Email local part too short, returning as-is");
            return email; // Too short to mask
        }
        
        String masked = localPart.charAt(0) + "****" +
                localPart.charAt(localPart.length() - 1) +
                "@" + domain;
        log.info("Email masked: '{}' -> '{}'", email, masked);
        return masked;
    }
    
    private static String maskString(String value) {
        log.debug("DataMasker.maskString() called with: {}", value);
        if (value == null || value.isEmpty()) {
            return value;
        }
        if (value.length() <= 2) {
            String masked = "**";
            log.info("String masked (short): '{}' -> '{}'", value, masked);
            return masked;
        }
        String masked = value.charAt(0) + "****" + value.charAt(value.length() - 1);
        log.info("String masked: '{}' -> '{}'", value, masked);
        return masked;
    }
}