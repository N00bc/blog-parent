package com.cyn.blog.anno;

import java.lang.annotation.*;

/**
 * @author Godc
 * Aop注解，
 * @date 2022/8/8 14:37
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnno {
    // 模块名
    String module() default "";

    // 功能
    String func() default "";
}
