package com.kp.rrm.autoconfigure.advice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kp.rrm.autoconfigure.security.DataEncryptor;
import com.kp.rrm.autoconfigure.security.DataMasker;
import com.kp.rrm.core.annotation.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ModelResponseBodyAdvice
        implements ResponseBodyAdvice<Object> {

    private static final Logger log =
            LoggerFactory.getLogger(ModelResponseBodyAdvice.class);

    private final ObjectMapper mapper;
    private final DataEncryptor encryptor;

    public ModelResponseBodyAdvice(ObjectMapper mapper,
                                   DataEncryptor encryptor) {
        this.mapper = mapper;
        this.encryptor = encryptor;
    }

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {

        return returnType.getExecutable()
                .isAnnotationPresent(ResponseModel.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType contentType,
                                  Class<? extends HttpMessageConverter<?>> converterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        log.info(">>> ModelResponseBodyAdvice.beforeBodyWrite() triggered <<<");

        if (body == null) return null;

        ResponseModel model =
                returnType.getExecutable()
                        .getAnnotation(ResponseModel.class);

        JsonNode root = mapper.valueToTree(body);
        traverse(root, model);
        return root;
    }

    private void traverse(JsonNode node, ResponseModel model) {

        if (node.isObject()) {
            apply((ObjectNode) node, model);
            node.fields().forEachRemaining(e ->
                    traverse(e.getValue(), model));
        }

        if (node.isArray()) {
            node.forEach(n -> traverse(n, model));
        }
    }

    private void apply(ObjectNode node, ResponseModel model) {

        for (String f : model.exclude()) node.remove(f);

        for (String f : model.mask())
            if (node.has(f))
                node.put(f, DataMasker.maskEmail(node.get(f).asText()));

        for (String f : model.encrypt())
            if (node.has(f))
                node.put(f, encryptor.encrypt(node.get(f).asText()));
    }
}

