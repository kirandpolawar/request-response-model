package com.kp.rrm.core.annotation;

import java.lang.annotation.*;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtraField {
    String name();
    Class<?> type();
}
