package com.kp.rrm.core.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResponseModel {

    Class<?> model();

    Class<?> responseType() default Object.class;

    String[] exclude() default {};

    String[] mask() default {};

    String[] encrypt() default {};
}


