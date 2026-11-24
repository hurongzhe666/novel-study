package io.github.hrz666.novelstudy.manager.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.hrz666.novelstudy.core.constant.DatabaseConsts;
import io.github.hrz666.novelstudy.dao.entity.UserInfo;
import io.github.hrz666.novelstudy.dao.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户模块 DAO管理类
 *
 * @author hrz666
 * @date 2026/11/18
 */
@Component
@RequiredArgsConstructor
public class UserDaoManager {

    private final UserInfoMapper userInfoMapper;

    /**
     * 根据用户ID集合批量查询用户信息列表
     *
     * @param userIds 需要查询的用户ID集合
     * @return 满足条件的用户信息列表
     */
    public List<UserInfo> listUsers(List<Long> userIds) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(), userIds);
        return userInfoMapper.selectList(queryWrapper);
    }

}
