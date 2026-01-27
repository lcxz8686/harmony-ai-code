package com.harmony.harmonyaicodeservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@MapperScan("com.harmony.harmonyaicodeservice.mapper")
public class HarmonyAiCodeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarmonyAiCodeServiceApplication.class, args);
    }
}
