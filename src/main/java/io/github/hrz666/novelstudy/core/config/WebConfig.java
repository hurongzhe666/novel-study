package io.github.hrz666.novelstudy.core.config;

import io.github.hrz666.novelstudy.core.constant.ApiRouterConsts;
import io.github.hrz666.novelstudy.core.constant.SystemConfigConsts;
import io.github.hrz666.novelstudy.core.interceptor.AuthInterceptor;
import io.github.hrz666.novelstudy.core.interceptor.FileInterceptor;
import io.github.hrz666.novelstudy.core.interceptor.FlowLimitInterceptor;
import io.github.hrz666.novelstudy.core.interceptor.TokenParseInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web Mvc 相关配置不要加 @EnableWebMvc 注解，否则会导致 jackson 的全局配置失效。因为 @EnableWebMvc 注解会导致
 * WebMvcAutoConfiguration 自动配置失效
 *
 * @author hrz666
 * @date 2022/5/18
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FlowLimitInterceptor flowLimitInterceptor;

    private final AuthInterceptor authInterceptor;

    private final FileInterceptor fileInterceptor;

    private final TokenParseInterceptor tokenParseInterceptor;

    /**
     * 上传根目录，配置文件里是 D:/novel-study-upload（结尾没有斜杠）。
     * 数据库里存的是 /localPic/2020/12/03/xxx.jpg 这种相对地址，
     * 要做静态映射就得靠它拼出磁盘上的真实位置。
     */
    @Value("${novel.file.upload.path}")
    private String fileUploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 流量限制拦截器
        registry.addInterceptor(flowLimitInterceptor)
            .addPathPatterns("/**")
            .order(0);

        // 文件访问拦截
        registry.addInterceptor(fileInterceptor)
            .addPathPatterns(SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY + "**")
            .order(1);

        // 权限认证拦截
        registry.addInterceptor(authInterceptor)
            // 拦截会员中心相关请求接口
            .addPathPatterns(ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/**",
                // 拦截图片上传接口，避免匿名用户持久化存储任意内容
                ApiRouterConsts.API_FRONT_RESOURCE_URL_PREFIX + "/image",
                // 拦截作家后台相关请求接口
                ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/**",
                // 拦截平台后台相关请求接口
                ApiRouterConsts.API_ADMIN_URL_PREFIX + "/**")
            // 放行登录注册相关请求接口
            .excludePathPatterns(ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/register",
                ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/login",
                ApiRouterConsts.API_ADMIN_URL_PREFIX + "/login")
            .order(2);

        // Token 解析拦截器
        registry.addInterceptor(tokenParseInterceptor)
            // 拦截小说内容查询接口，需要解析 token 以判断该用户是否有权阅读该章节（付费章节是否已购买）
            .addPathPatterns(ApiRouterConsts.API_FRONT_BOOK_URL_PREFIX + "/content/*")
            .order(3);

    }

    /**
     * 静态资源映射：把 URL 路径映射到磁盘目录。
     *
     * 为什么需要：数据库 book_info.pic_url 存的是 /localPic/2020/12/03/xxx.jpg，
     * 前端会拼成 http://127.0.0.1:8888/localPic/... 来取图。
     * 但 Spring Boot 默认只把 classpath 下的 static/public 目录当静态资源，
     * D 盘上的文件夹它不认识，不配这一手就是满屏 404 裂图。
     *
     * 注意：addResourceLocations 结尾的 "/" 不能少，否则最后一个目录会被当成文件名。
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/localPic/**", "/images/**")
            .addResourceLocations("file:" + fileUploadPath + "/");
    }
}
