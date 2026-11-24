package io.github.hrz666.novelstudy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dao.entity.UserInfo;
import io.github.hrz666.novelstudy.dto.req.UserLoginReqDto;
import io.github.hrz666.novelstudy.dto.resp.UserLoginRespDto;
import io.github.hrz666.novelstudy.dto.resp.UserRegisterRespDto;
import org.springframework.stereotype.Service;


public interface UserService{

    /**
     * 用户登录
     * @param userLoginReqDto
     * @return
     */
    RestResp<UserLoginRespDto> login(UserLoginReqDto userLoginReqDto);

    /**
     * 用户注册
     * @param userRegisterRespDto
     * @return
     */
    RestResp<UserRegisterRespDto> register(UserRegisterRespDto userRegisterRespDto);
}
