package com.kp.rrm.autoconfigure.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.rrm.autoconfigure.security.DataEncryptor;
import com.kp.rrm.core.annotation.ResponseModel;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IMPORTANT:
 * ------------
 * This Aspect MUST NOT modify or return controller responses.
 * Response transformation is handled by ResponseBodyAdvice.
 */
@Aspect
public class ModelAspect {

    private static final Logger log = LoggerFactory.getLogger(ModelAspect.class);

    private final ObjectMapper mapper;
    private final DataEncryptor encryptor;

    public ModelAspect(ObjectMapper mapper, DataEncryptor encryptor) {
        log.info("========================================");
        log.info("ModelAspect constructor called!");
        log.info("ObjectMapper: {}", mapper != null ? "provided" : "NULL");
        log.info("DataEncryptor: {}", encryptor != null ? "provided" : "NULL");
        log.info("========================================");
        this.mapper = mapper;
        this.encryptor = encryptor;
    }

    /**
     * Lifecycle hook only.
     * NO response mutation allowed here.
     */
    @Before("@annotation(model)")
    public void beforeResponse(ResponseModel model) {
        log.info("========================================");
        log.info("ModelAspect.beforeResponse() triggered");
        log.info("ResponseModel detected:");
        log.info("  - Exclude fields: {}", java.util.Arrays.toString(model.exclude()));
        log.info("  - Mask fields   : {}", java.util.Arrays.toString(model.mask()));
        log.info("  - Encrypt fields: {}", java.util.Arrays.toString(model.encrypt()));
        log.info("========================================");
    }
}
