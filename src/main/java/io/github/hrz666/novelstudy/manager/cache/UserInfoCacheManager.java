package io.github.hrz666.novelstudy.manager.cache;

import io.github.hrz666.novelstudy.core.constant.CacheConsts;
import io.github.hrz666.novelstudy.dao.entity.UserInfo;
import io.github.hrz666.novelstudy.dao.mapper.UserInfoMapper;
import io.github.hrz666.novelstudy.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户信息 缓存管理类
 *
 * @author hrz666
 * @date 2026/11/18
 */
@Component
@RequiredArgsConstructor
public class UserInfoCacheManager {

    private final UserInfoMapper userInfoMapper;

    /**
     * 查询用户信息，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
        value = CacheConsts.USER_INFO_CACHE_NAME)
    public UserInfoDto getUser(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return UserInfoDto.builder()
            .id(userInfo.getId())
            .status(userInfo.getStatus()).build();
    }


}
