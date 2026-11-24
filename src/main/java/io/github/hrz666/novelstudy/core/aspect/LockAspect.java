package io.github.hrz666.novelstudy.core.aspect;

import io.github.hrz666.novelstudy.core.annotation.Key;
import io.github.hrz666.novelstudy.core.annotation.Lock;
import io.github.hrz666.novelstudy.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁 切面
 * 这个类本职是在将加了key注解的地方上面的注解信息和真实参数拿出来
 * 拼接成规则的key放入redis做成锁
 * 然后去尝试获取锁
 * 如果获取锁成功则执行原方法
 * 最后释放锁
 *
 * @author hrz666
 * @date 2026/11/20
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LockAspect {

    private final RedissonClient redissonClient;

    private static final String KEY_PREFIX = "Lock";

    private static final String KEY_SEPARATOR = "::";

    @Around(value = "@annotation(io.github.hrz666.novelstudy.core.annotation.Lock)")
    @SneakyThrows
    public Object doAround(ProceedingJoinPoint joinPoint) {
        //用来获取“这次调用的是哪个方法”的信息。
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取目标方法
        Method targetMethod = methodSignature.getMethod();
        //获取目标方法上的Lock注解
        Lock lock = targetMethod.getAnnotation(Lock.class);
        //构建锁的key
        String lockKey = KEY_PREFIX + buildLockKey(lock.prefix(), targetMethod,
            joinPoint.getArgs());
        //获取锁
        RLock rLock = redissonClient.getLock(lockKey);
        //尝试获取锁
        if (lock.isWait() ? rLock.tryLock(lock.waitTime(), TimeUnit.SECONDS) : rLock.tryLock()) {
            //获取锁成功
            try {
                //执行原需要加锁的方法
                return joinPoint.proceed();
            } finally {
                //释放锁
                rLock.unlock();
            }
        }
        throw new BusinessException(lock.failCode());
    }

    //构建锁的key
    private String buildLockKey(String prefix, Method method, Object[] args) {
        StringBuilder builder = new StringBuilder();
        //拼接前缀
        if (StringUtils.hasText(prefix)) {
            builder.append(KEY_SEPARATOR).append(prefix);
        }
        //告诉我这个方法的参数位置有没有贴Key注解，把参数位置用数组表示
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //拼接参数
            builder.append(KEY_SEPARATOR);
            //查看是否贴了Key注解
            if (parameters[i].isAnnotationPresent(Key.class)) {
                // 获取标签上的 expr 属性值。这一步是为了读它里面的 expr
                Key key = parameters[i].getAnnotation(Key.class);
                //把公式算成值
                builder.append(parseKeyExpr(key.expr(), args[i])); //args[i]告诉我这个参数位置放的是什么值
            }
        }
        return builder.toString();
    }

    //解析锁的key
    private String parseKeyExpr(String expr, Object arg) {
        if (!StringUtils.hasText(expr)) {
            return arg.toString();    // ① 注解没写公式 → 直接用参数本身
        }
        ExpressionParser parser = new SpelExpressionParser();   // ② 备好计算器
        Expression expression = parser.parseExpression(expr, new TemplateParserContext());  // ③ 读懂公式（还没算）
        return expression.getValue(arg, String.class);   // ④ 代入真值，算出结果
    }

}
