package io.github.hrz666.novelstudy.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.hrz666.novelstudy.core.common.constant.ErrorCodeEnum;
import io.github.hrz666.novelstudy.core.common.exception.BusinessException;
import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.core.constant.DatabaseConsts;
import io.github.hrz666.novelstudy.core.constant.SystemConfigConsts;
import io.github.hrz666.novelstudy.core.util.JwtUtils;
import io.github.hrz666.novelstudy.dao.entity.UserInfo;
import io.github.hrz666.novelstudy.dao.mapper.UserBookshelfMapper;
import io.github.hrz666.novelstudy.dao.mapper.UserFeedbackMapper;
import io.github.hrz666.novelstudy.dao.mapper.UserInfoMapper;
import io.github.hrz666.novelstudy.dto.req.UserLoginReqDto;
import io.github.hrz666.novelstudy.dto.req.UserRegisterReqDto;
import io.github.hrz666.novelstudy.dto.resp.UserLoginRespDto;
import io.github.hrz666.novelstudy.dto.resp.UserRegisterRespDto;
import io.github.hrz666.novelstudy.manager.redis.VerifyCodeManager;
import io.github.hrz666.novelstudy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
//继承总的Serviceimpl
public class UserServiceImpl implements UserService {
    private final UserInfoMapper userInfoMapper;

    private final VerifyCodeManager verifyCodeManager;

    private final UserFeedbackMapper userFeedbackMapper;

    private final UserBookshelfMapper userBookshelfMapper;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 32 位十六进制字符串，即旧版无盐 MD5 密码摘要的格式
     */
    private static final Pattern MD5_HEX_PATTERN = Pattern.compile("^[0-9a-f]{32}$");

    /**
     * 判断密码是否为 BCrypt 哈希
     */
    private static boolean isBCryptHash(String storedPassword) {
        return !MD5_HEX_PATTERN.matcher(storedPassword).matches();
    }


    /**
     * 注册
     */
    public RestResp<UserRegisterRespDto> register(UserRegisterRespDto userRegisterRespDto) {
        UserInfo userInfo = new UserInfo();
        return RestResp.ok(UserRegisterRespDto.builder()
                .uid(userInfo.getId())
                .token("token").build());
    }


    /**
     * 登录
     */
    public RestResp<UserLoginRespDto> login(UserLoginReqDto userLoginReqDto) {
        //1.先用wrapper查询出用户
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, userLoginReqDto.getUsername())
                    .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());

        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        //1.1 判断用户是否存在
        if (Objects.isNull(userInfo)) {
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }

        //2.判断密码是否正确
        /*
        *原版本使用的是MD5算法来加密密码，新版使用的是新的BCrypt算法，
        *这里需要判断加密算法是哪一种类型，然后根据不同加密方法来先判断密码是否正确
        *如果是 BCrypt 哈希，则使用 passwordEncoder 匹配
        *如果是 MD5 哈希，需要先使用MD5算法将密码先对比出来，再将MD5升级为BCrypt哈希
        *BCrypt算法，每次加密的时候都会加一把盐，同一个密码加密出来永远不一样，所有要先把盐析出，然后根据盐重新将密码再加密一遍再对比
        */

        // 获取存储的密码
        String storedPassword = userInfo.getPassword();
        boolean passwordOk;
        if (isBCryptHash(storedPassword)) {
            //BCrypt 哈希
            passwordOk = passwordEncoder.matches(userLoginReqDto.getPassword(), storedPassword);
        } else {
            //MD5 哈希
            passwordOk = Objects.equals(storedPassword,
                    DigestUtils.md5DigestAsHex(userLoginReqDto.getPassword().getBytes(StandardCharsets.UTF_8)));
            if (passwordOk) {
                // 登录成功，顺带把旧的无盐 MD5 哈希升级为 BCrypt
                UserInfo upgrade = new UserInfo();
                upgrade.setId(userInfo.getId());
                upgrade.setPassword(passwordEncoder.encode(userLoginReqDto.getPassword()));
                upgrade.setUpdateTime(LocalDateTime.now());
                userInfoMapper.updateById(upgrade);
            }
        }
        if (!passwordOk) {
            // 密码错误
            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
        }

        //3.生成token,并返回
        return RestResp.ok(UserLoginRespDto.builder()
                .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
                .uid(userInfo.getId())
                .nickName(userInfo.getNickName())
                .build());
    }


}
