package io.github.hrz666.novelstudy.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 作用是在 @Lock 方法里指出「哪几个参数参与拼锁名」
 *
 * @author hrz666
 * @date 2026/11/19
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Key {

    String expr() default "";

}
