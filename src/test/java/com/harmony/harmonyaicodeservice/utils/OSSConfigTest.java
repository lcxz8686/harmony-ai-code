package com.harmony.harmonyaicodeservice.utils;

import cn.hutool.core.io.FileUtil;
import com.harmony.harmonyaicodeservice.manager.OSSManager;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@SpringBootTest
class OSSConfigTest {

    @Resource
    private OSSManager ossManager;

    /**
     * 测试截图上传OSS
     */
    @Test
    void testScreenshotUploadOSS() {
        String url = "https://www.baidu.com";
        String path = WebScreenshotUtil.saveWebPageScreenshot(url);
        File file = FileUtil.file(path);
        String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = UUID.randomUUID().toString().substring(0, 8) + "_compressed.jpg";
        String fileUrl = ossManager.uploadFile("screenshot/" + prefix + fileName, file);
        System.out.println(fileUrl);
    }
}
