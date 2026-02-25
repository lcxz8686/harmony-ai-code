package com.harmony.harmonyaicodeservice.core;

import com.harmony.harmonyaicodeservice.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

//    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("任务记录网站, 代码不用太复杂，不要操作100行", CodeGenTypeEnum.MULTI_FILE, 1L);
        Assertions.assertNotNull(file);
    }

//    @Test
    void generateAndSaveCode1() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("实现一个流行坠落的页面，需要好看美观，不要操作500行", CodeGenTypeEnum.MULTI_FILE, 1L);
        Assertions.assertNotNull(file);
    }

//    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream("任务记录网站", CodeGenTypeEnum.MULTI_FILE, 1L);
        // 阻塞等待所有数据收集完成
        List<String> result = codeStream.collectList().block();
        // 验证结果
        Assertions.assertNotNull(result);
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }

    @Test
    void generateVueProjectCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream(
                "简单的任务记录网站，总代码量不超过 200 行",
                CodeGenTypeEnum.VUE_PROJECT, 1L);
        // 阻塞等待所有数据收集完成
        List<String> result = codeStream.collectList().block();
        // 验证结果
        Assertions.assertNotNull(result);
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }

}
