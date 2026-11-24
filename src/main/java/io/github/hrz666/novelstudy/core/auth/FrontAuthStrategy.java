package io.github.hrz666.novelstudy.core.auth;

import io.github.hrz666.novelstudy.core.common.exception.BusinessException;
import io.github.hrz666.novelstudy.core.util.JwtUtils;
import io.github.hrz666.novelstudy.manager.cache.UserInfoCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 前台门户系统 认证授权策略
 *
 * @author hrz666
 * @date 2026/11/19
 */
@Component
@RequiredArgsConstructor
public class FrontAuthStrategy implements AuthStrategy {

    private final JwtUtils jwtUtils;

    private final UserInfoCacheManager userInfoCacheManager;

    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        // 统一账号认证
        authSSO(jwtUtils, userInfoCacheManager, token);
    }

}