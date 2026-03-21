package com.harmony.harmonyaicodeservice.manager;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.harmony.harmonyaicodeservice.utils.OSSClientConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

/**
 * OSS文件管理
 *
 * @author: <a href="https://gitee.com/Harmony_TL">harmony</a>
 * @DateTime: 2026-03-20
 */
@Slf4j
@Component
public class OSSManager {

    @Resource
    private OSS ossClient;

    @Resource
    private OSSClientConfig ossClientConfig;

    /**
     * 上传文件到OSS，返回文件URL
     * @return
     */
    public String uploadFile(String key, File file) {
        try {
            PutObjectResult result = ossClient.putObject(ossClientConfig.getBucketName(), key, file);
            String fileUrl = getFileUrl(key);
            log.info("文件上传成功，URL: {}, ETag: {}", fileUrl, result.getETag());
            return fileUrl;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 下载文件到本地
     * @param objectKey
     * @return
     */
    public InputStream downloadFile(String objectKey) {
        try {
            OSSObject ossObject = ossClient.getObject(ossClientConfig.getBucketName(), objectKey);
            return ossObject.getObjectContent();
        } catch (Exception e) {
            log.error("文件下载失败", e);
            throw new RuntimeException("文件下载失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从OSS删除文件
     * @param objectKey
     */
    public void deleteFile(String objectKey) {
        try {
            ossClient.deleteObject(ossClientConfig.getBucketName(), objectKey);
            log.info("文件删除成功: {}", objectKey);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new RuntimeException("文件删除失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查文件是否存在
     * @param objectKey
     * @return
     */
    public boolean fileExists(String objectKey) {
        try {
            return ossClient.doesObjectExist(ossClientConfig.getBucketName(), objectKey);
        } catch (Exception e) {
            log.error("检查文件是否存在失败", e);
            return false;
        }
    }

    public String getFileUrl(String objectKey) {
        return String.format("https://%s.%s/%s", ossClientConfig.getBucketName(), ossClientConfig.getEndpoint(), objectKey);
    }
}
