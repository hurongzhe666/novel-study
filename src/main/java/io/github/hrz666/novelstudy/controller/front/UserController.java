package io.github.hrz666.novelstudy.controller.front;


import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.core.constant.ApiRouterConsts;
import io.github.hrz666.novelstudy.core.constant.SystemConfigConsts;
import io.github.hrz666.novelstudy.dto.UserInfoDto;
import io.github.hrz666.novelstudy.dto.req.UserLoginReqDto;
import io.github.hrz666.novelstudy.dto.req.UserRegisterReqDto;
import io.github.hrz666.novelstudy.dto.resp.UserLoginRespDto;
import io.github.hrz666.novelstudy.dto.resp.UserRegisterRespDto;
import io.github.hrz666.novelstudy.service.UserService;
import io.github.hrz666.novelstudy.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserController", description = "前台门户-会员模块")
@SecurityRequirement(name = SystemConfigConsts.HTTP_AUTH_HEADER_NAME) //在 Swagger 接口文档页面上加一把“小锁”图标，告诉文档读者“这个接口要带 token 才能调”
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterReqDto
     * @return
     */
    @Operation(summary = "用户注册接口")
    @PostMapping("register") //@Valid 让 DTO 字段上写好的校验规则真正生效。
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto userRegisterReqDto){
        log.info("用户注册: {}", userRegisterReqDto);
        return userService.register(userRegisterReqDto);
    }

    /**
     * 用户登录
     * @param userLoginReqDto
     * @return
     */
    @Operation(summary = "用户登录接口")
    @PostMapping("login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto userLoginReqDto){
        log.info("用户登录: {}", userLoginReqDto);
        return userService.login(userLoginReqDto);
    }
}
