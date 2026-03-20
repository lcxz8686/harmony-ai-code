package com.harmony.harmonyaicodeservice.service;

/**
 * 截图服务接口
 *
 * @author: <a href="https://gitee.com/Harmony_TL">harmony</a>
 * @DateTime: 2026-03-20
 */
public interface ScreenshotService {

    /**
     * 截图并上传到阿里云OSS
     * @param url
     * @return
     */
    String generateAndUploadScreenshot(String url);
}
