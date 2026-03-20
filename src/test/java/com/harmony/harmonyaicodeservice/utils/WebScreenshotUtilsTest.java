package com.harmony.harmonyaicodeservice.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScreenshotUtilsTest {

    @Test
    void saveWebPageScreenshot() {
        String url = "https://www.baidu.com";
        String path = WebScreenshotUtil.saveWebPageScreenshot(url);
        System.out.println(path);
        Assertions.assertNotNull(path);
    }
}