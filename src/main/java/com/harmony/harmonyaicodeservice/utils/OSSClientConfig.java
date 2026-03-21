package com.harmony.harmonyaicodeservice.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Data
public class OSSClientConfig {

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucket-name}")
    private String bucketName;

    @Value("${oss.access-key}")
    private String accessKey;

    @Value("${oss.secret-key}")
    private String secretKey;

    @Bean
    public OSS ossClient() {
        try {
            DefaultCredentialProvider credentialsProvider = new DefaultCredentialProvider(accessKey, secretKey);
            log.info("OSS客户端初始化成功，endpoint: {}, bucket: {}", endpoint, bucketName);
            return new OSSClientBuilder().build(endpoint, credentialsProvider);
        } catch (Exception e) {
            log.error("OSS客户端初始化失败", e);
            throw new RuntimeException("OSS客户端初始化失败", e);
        }
    }
}
