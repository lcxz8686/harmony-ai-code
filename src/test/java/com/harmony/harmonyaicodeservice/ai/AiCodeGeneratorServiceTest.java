package com.harmony.harmonyaicodeservice.ai;

import com.harmony.harmonyaicodeservice.ai.model.HtmlCodeResult;
import com.harmony.harmonyaicodeservice.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

//    @Test
    void generateHtmlCode() {
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode("做个程序员鱼皮的博客，不超过 20 行");
        Assertions.assertNotNull(htmlCodeResult);
    }

//    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode("做个程序员鱼皮的留言板");
        Assertions.assertNotNull(multiFileCodeResult);
    }
}