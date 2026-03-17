package com.harmony.harmonyaicodeservice.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScreenshotUtilsTest {

    @Test
    void saveWebPageScreenshot() {
        String url = "https://www.baidu.com";
        String path = WebScreenshotUtils.saveWebPageScreenshot(url);
        Assertions.assertNotNull(path);
    }
}