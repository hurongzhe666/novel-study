package io.github.hrz666.novelstudy.core.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件 拦截器
 * 把磁盘上的图片直接读出来写进响应，不走 Controller
 * 这个拦截器解决的是上传的图片存进磁盘后，怎么被浏览器访问到的问题
 * @author hrz666
 * @date 2026/11/20
 */
@Component
@RequiredArgsConstructor
public class FileInterceptor implements HandlerInterceptor {

    @Value("${novel.file.upload.path}")
    private String fileUploadPath;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        // 获取请求的 URI
        String requestUri = request.getRequestURI();
        // 缓存10天
        response.setDateHeader("expires", System.currentTimeMillis() + 60 * 60 * 24 * 10 * 1000);
        try (OutputStream out = response.getOutputStream(); InputStream input = new FileInputStream(
            fileUploadPath + requestUri)) {
            byte[] b = new byte[4096];
            for (int n; (n = input.read(b)) != -1; ) {
                out.write(b, 0, n);
            }
        }
        return false;
    }

}
