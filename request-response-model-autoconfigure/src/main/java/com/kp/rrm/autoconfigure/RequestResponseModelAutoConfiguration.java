package com.kp.rrm.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.rrm.autoconfigure.advice.ModelResponseBodyAdvice;
import com.kp.rrm.autoconfigure.aspect.ModelAspect;
import com.kp.rrm.autoconfigure.config.EncryptionProperties;
import com.kp.rrm.autoconfigure.security.DataEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@AutoConfiguration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties(EncryptionProperties.class)
public class RequestResponseModelAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseModelAutoConfiguration.class);

    public RequestResponseModelAutoConfiguration() {
        log.info("========================================");
        log.info("RequestResponseModelAutoConfiguration is being loaded!");
        log.info("========================================");
    }

    @Bean
    public DataEncryptor dataEncryptor(EncryptionProperties props) {
        log.info("Creating DataEncryptor bean with key length: {}", props.getKey() != null ? props.getKey().length() : 0);
        DataEncryptor encryptor = new DataEncryptor(props.getKey());
        log.info("DataEncryptor bean created successfully");
        return encryptor;
    }

    @Bean
    public ModelResponseBodyAdvice modelResponseBodyAdvice(
            ObjectMapper mapper,
            DataEncryptor encryptor) {

        return new ModelResponseBodyAdvice(mapper, encryptor);
    }

    @Bean
    public ModelAspect modelAspect(
            ObjectMapper mapper,
            DataEncryptor encryptor
    ) {
        log.info("Creating ModelAspect bean");
        log.info("ObjectMapper: {}", mapper != null ? "provided" : "NULL");
        log.info("DataEncryptor: {}", encryptor != null ? "provided" : "NULL");
        ModelAspect aspect = new ModelAspect(mapper, encryptor);
        log.info("ModelAspect bean created successfully");
        return aspect;
    }
}

