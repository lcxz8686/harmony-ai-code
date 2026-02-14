package com.harmony.harmonyaicodeservice.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 部署请求类
 *
 * @author: <a href="https://gitee.com/Harmony_TL">harmony</a>
 * @DateTime: 2026-02-09
 */
@Data
public class AppDeployRequest implements Serializable {

    /**
     * 应用 id
     */
    private Long appId;

    private static final long serialVersionUID = 1L;
}
