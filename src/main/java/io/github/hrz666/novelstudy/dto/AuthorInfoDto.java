package io.github.hrz666.novelstudy.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 作家信息 DTO，缓存中间类，用来确定用户是否登录
 *
 * @author hrz666
 * @date 2026/11/18
 */
@Data
@Builder
public class AuthorInfoDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String penName;

    private Integer status;

}
