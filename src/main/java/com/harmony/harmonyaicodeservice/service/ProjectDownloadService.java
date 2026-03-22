package com.harmony.harmonyaicodeservice.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 *
 *
 * @author: <a href="https://gitee.com/Harmony_TL">harmony</a>
 * @DateTime: 2026-03-22
 */
public interface ProjectDownloadService {
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
