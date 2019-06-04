package com.br.log.annotation;

import java.lang.annotation.*;

/**
 * 业务操作注解
 *
 * @Author Zero
 * @Date 2019 06 04
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BizOperation {
    String value() default "";
}
