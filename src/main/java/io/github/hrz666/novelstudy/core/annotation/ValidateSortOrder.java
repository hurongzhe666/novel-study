package io.github.hrz666.novelstudy.core.annotation;

import java.lang.annotation.*;

/**
 *
 * 用来标记需要白名单的mapper的位置，然后在白名单类得里面需要用这个注解来定位
 * 拦截并清洗 SQL 排序字段，防注入
 *
 * @author hrz666
 * @date 2026/11/19
 */
@Target(ElementType.PARAMETER) // 表示该注解只能用于方法参数上
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时依然可用，便于 AOP 或其他框架读取
@Documented // 该注解将被包含在生成的 Javadoc 中
public @interface ValidateSortOrder {

}
