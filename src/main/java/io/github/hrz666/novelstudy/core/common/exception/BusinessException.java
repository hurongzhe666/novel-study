package io.github.hrz666.novelstudy.core.common.exception;

import io.github.hrz666.novelstudy.core.common.constant.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义业务异常，用于处理用户请求时，业务错误时抛出
 *
 * @author hrz666
 * @date 2026/11/14
 */
@Data
@EqualsAndHashCode(callSuper = true) // 继承父类的equals和hashCode方法
public class BusinessException extends RuntimeException {
    private final ErrorCodeEnum errorCodeEnum;


    public BusinessException(ErrorCodeEnum errorCodeEnum) {

        //super(message, cause, enableSuppression, writableStackTrace);
        super(errorCodeEnum.getMessage(),null,false,false);
        this.errorCodeEnum = errorCodeEnum;
    }
}
