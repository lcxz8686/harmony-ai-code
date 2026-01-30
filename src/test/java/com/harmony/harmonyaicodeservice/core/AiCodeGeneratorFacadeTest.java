package com.harmony.harmonyaicodeservice.core;

import com.harmony.harmonyaicodeservice.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("任务记录网站, 代码不用太复杂，不要操作100行", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCode1() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("实现一个流行坠落的页面，需要好看美观，不要操作500行", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(file);
    }
}
