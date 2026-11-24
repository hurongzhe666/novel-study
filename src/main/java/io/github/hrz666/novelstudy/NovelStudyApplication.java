package io.github.hrz666.novelstudy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@SpringBootApplication
@MapperScan("io.github.hrz666.novelstudy.dao.mapper")   // ① 扫 Mapper 接口
@RequiredArgsConstructor
public class NovelStudyApplication implements CommandLineRunner {   // ② 启动后回调

    private final DataSource dataSource;          // ③ Spring Boot 自动配好的连接池
    private final StringRedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(NovelStudyApplication.class, args);
    }

    /**
     * Spring Security 的管辖范围声明（照原版 NovelApplication 敲的）。
     *
     * 为什么必须有这个 Bean：pom 里引了 spring-boot-starter-security（用来保护 actuator），
     * 框架的规矩是——classpath 里有我、而你又没写任何安全配置，我就锁死一切请求。
     * 不写这个 Bean，所有业务接口（包括登录注册）都会被 401 + WWW-Authenticate: Basic 拦下，
     * 请求根本进不了你自己的 AuthInterceptor。
     *
     * securityMatcher(EndpointRequest.toAnyEndpoint()) 把 Spring Security 的活动范围
     * 圈定在 /actuator/** 一亩三分地内，业务接口一概不管——业务接口的鉴权
     * 走项目自己的 JWT 拦截器链（AuthInterceptor），两套体系互不掺和。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .securityMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeHttpRequests(requests -> requests.anyRequest().hasRole("ENDPOINT_ADMIN"));
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Override
    public void run(String... args) throws Exception {
        // ④ 验证 MySQL
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("select version()")) {
            if (rs.next()) {
                log.info("MySQL 连接成功，版本：{}", rs.getString(1));
            }
        }
        // ⑤ 验证 Redis
        redisTemplate.opsForValue().set("novel:ping", "pong");
        log.info("Redis 连接成功，读到：{}", redisTemplate.opsForValue().get("novel:ping"));
    }
}
