package io.github.hrz666.novelstudy.core.annotation;


import io.github.hrz666.novelstudy.core.common.constant.ErrorCodeEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 提醒这个方法加锁，防止重复提交
 *
 * @author hrz666
 * @date 2026/11/19
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD) // 这个注解只能加在方法上
public @interface Lock {

    String prefix(); // 锁名前缀，如 "userComment"

    boolean isWait() default false; // 抢不到锁时等不等

    long waitTime() default 3L; // 抢不到锁时等3秒

    ErrorCodeEnum failCode() default ErrorCodeEnum.OK; // 抢不到锁时返回的错误码

}