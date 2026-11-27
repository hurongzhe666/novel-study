package io.github.hrz666.novelstudy.service.impl;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.github.hrz666.novelstudy.core.common.constant.ErrorCodeEnum;
import io.github.hrz666.novelstudy.core.common.exception.BusinessException;
import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.core.constant.SystemConfigConsts;
import io.github.hrz666.novelstudy.dto.resp.ImgVerifyCodeRespDto;
import io.github.hrz666.novelstudy.manager.redis.VerifyCodeManager;
import io.github.hrz666.novelstudy.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j

public class ResourceServiceImpl implements ResourceService {

    /*
     * 1. 前端调 /api/front/resource/img_verify_code
     * 2. 后端: sessionId = IdWorker.get32UUID()   ← 摇一个随机号
     *         code = ImgVerifyCodeUtils.getRandomVerifyCode(4)  ← 摇4位数字
     *         Redis 写入: key = IMG_VERIFY_CODE_CACHE_KEY + sessionId
     *                     value = code, TTL 5分钟
     *         返回给前端: { sessionId: "a3f...", img: "data:image/jpeg;base64,..." }
     * 3. 用户注册时把 sessionId + 用户填的验证码 一起 POST 回来
     * 4. 后端拿 sessionId 拼 key 去 Redis 查出正确答案，对比
     * 5. 对完立刻 delete 这个 key（用完即删，防止同一个验证码重复用）
     */

    private final VerifyCodeManager verifyCodeManager;

    @Value("${novel.file.upload.path}")
    private String fileUploadPath;

    @Override
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {
        String sessionId = IdWorker.get32UUID();
        return RestResp.ok(ImgVerifyCodeRespDto.builder()
                .sessionId(sessionId)
                .img(verifyCodeManager.genImgVerifyCode(sessionId))
                .build());
    }

    @SneakyThrows
    @Override
    //图片自身自带路径，通过判断输入输出在前端还是后端来拼接固定路径
    //在后端拼“磁盘真实位置”（你的 WebConfig.addResourceHandlers）
    //而前端则用前端规定的方法
    public RestResp<String> uploadImage(MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        //图片本身的固定路径字段
        String savePath =
                SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY
                        + now.format(DateTimeFormatter.ofPattern("yyyy")) + File.separator
                        + now.format(DateTimeFormatter.ofPattern("MM")) + File.separator
                        + now.format(DateTimeFormatter.ofPattern("dd"));
        String oriName = file.getOriginalFilename();
        assert oriName != null;
        // 在落盘前先解码校验，避免任意内容先持久化到磁盘后才被发现不是图片
        if (Objects.isNull(ImageIO.read(file.getInputStream()))) {
            // 上传的文件不是图片
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_TYPE_NOT_MATCH);
        }
        String saveFileName = IdWorker.get32UUID() + oriName.substring(oriName.lastIndexOf("."));
        File saveFile = new File(fileUploadPath + savePath, saveFileName);
        if (!saveFile.getParentFile().exists()) {
            boolean isSuccess = saveFile.getParentFile().mkdirs();
            if (!isSuccess) {
                throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
            }
        }
        file.transferTo(saveFile);
        return RestResp.ok(savePath + File.separator + saveFileName);
    }
}






