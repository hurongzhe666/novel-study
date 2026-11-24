package io.github.hrz666.novelstudy.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 用户注册 响应DTO
 * @author hrz666
 * @date 2026/11/18
 */
@Data
@Builder
public class UserRegisterRespDto {

    @Schema(description = "用户ID")
    private Long uid;

    @Schema(description = "用户token")
    private String token;
}
