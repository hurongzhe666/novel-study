package io.github.hrz666.novelstudy.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mail 配置属性
 *
 * @author hrz666
 * @date 2023/3/25
 */
@ConfigurationProperties(prefix = "spring.mail")
public record MailProperties(String nickname, String username) {

}
