package com.kp.rrm.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestModel {
    Class<?> model();
    String[] exclude() default {};
    ExtraField[] add() default {};
}
