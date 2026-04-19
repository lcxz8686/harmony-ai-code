package com.harmony.harmonyaicodeservice.core.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.harmony.harmonyaicodeservice.ai.model.message.*;
import com.harmony.harmonyaicodeservice.ai.tools.BaseTool;
import com.harmony.harmonyaicodeservice.ai.tools.ToolManager;
import com.harmony.harmonyaicodeservice.core.builder.VueProjectBuilder;
import com.harmony.harmonyaicodeservice.mapper.AppMapper;
import com.harmony.harmonyaicodeservice.model.constant.AppConstant;
import com.harmony.harmonyaicodeservice.model.entity.App;
import com.harmony.harmonyaicodeservice.model.entity.User;
import com.harmony.harmonyaicodeservice.model.enums.ChatHistoryMessageTypeEnum;
import com.harmony.harmonyaicodeservice.service.ChatHistoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * JSON 消息流处理器
 * 处理 VUE_PROJECT 类型的复杂流式响应，包含工具调用信息
 */
@Slf4j
@Component
public class JsonMessageStreamHandler {

    @Resource
    private VueProjectBuilder vueProjectBuilder;

    @Resource
    private AppMapper appMapper;

    @Resource
    private ToolManager toolManager;

    /**
     * 处理 TokenStream（VUE_PROJECT）
     * 解析 JSON 消息并重组为完整的响应格式
     *
     * @param originFlux         原始流
     * @param chatHistoryService 聊天历史服务
     * @param appId              应用ID
     * @param loginUser          登录用户
     * @return 处理后的流
     */
    public Flux<String> handle(Flux<String> originFlux,
                               ChatHistoryService chatHistoryService,
                               long appId, User loginUser) {
        // 收集数据用于生成后端记忆格式
        StringBuilder chatHistoryStringBuilder = new StringBuilder();
        // 用于跟踪已经见过的工具ID，判断是否是第一次调用
        Set<String> seenToolIds = new HashSet<>();
        return originFlux
                .map(chunk -> {
                    // 解析每个 JSON 消息块
                    log.info("收到消息 chunk: {}", chunk);
                    return handleJsonMessageChunk(chunk, chatHistoryStringBuilder, seenToolIds);
                })
                .filter(StrUtil::isNotEmpty) // 过滤空字串
                .doOnComplete(() -> {
                    // 流式响应完成后，添加 AI 消息到对话历史
                    String aiResponse = chatHistoryStringBuilder.toString();
                    chatHistoryService.addChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                    // Vue 项目生成完成后，构建并部署
                    buildAndDeployVueProject(appId);
                })
                .doOnError(error -> {
                    // 如果AI回复失败，也要记录错误消息
                    String errorMessage = "AI回复失败: " + error.getMessage();
                    chatHistoryService.addChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                });
    }

    /**
     * 解析并收集 TokenStream 数据
     */
    private String handleJsonMessageChunk(String chunk, StringBuilder chatHistoryStringBuilder, Set<String> seenToolIds) {
        // 解析 JSON
        StreamMessage streamMessage = JSONUtil.toBean(chunk, StreamMessage.class);
        StreamMessageTypeEnum typeEnum = StreamMessageTypeEnum.getEnumByValue(streamMessage.getType());
        log.info("收到消息: {}", streamMessage.getType());
        switch (typeEnum) {
            case AI_RESPONSE -> {
                AiResponseMessage aiMessage = JSONUtil.toBean(chunk, AiResponseMessage.class);
                String data = aiMessage.getData();
                // 直接拼接响应
                chatHistoryStringBuilder.append(data);
                return data;
            }
            case TOOL_REQUEST -> {
                ToolRequestMessage toolRequestMessage = JSONUtil.toBean(chunk, ToolRequestMessage.class);
                String toolId = toolRequestMessage.getId();
                String toolName = toolRequestMessage.getName();
                // 检查是否是第一次看到这个工具 ID
                if (toolId != null && !seenToolIds.contains(toolId)) {
                    // 第一次调用这个工具，记录 ID 并返回工具信息
                    seenToolIds.add(toolId);
                    // 根据工具名称获取工具实例
                    BaseTool tool = toolManager.getTool(toolName);
                    // 返回格式化的工具调用信息
                    return tool.generateToolRequestResponse();
                } else {
                    // 不是第一次调用这个工具，直接返回空
                    return "";
                }
            }
            case TOOL_EXECUTED -> {
                ToolExecutedMessage toolExecutedMessage = JSONUtil.toBean(chunk, ToolExecutedMessage.class);
                String toolName = toolExecutedMessage.getName();
                JSONObject jsonObject = JSONUtil.parseObj(toolExecutedMessage.getArguments());
                // 根据工具名称获取工具实例并生成相应的结果格式
                BaseTool tool = toolManager.getTool(toolName);
                String result = tool.generateToolExecutedResult(jsonObject);
                // 输出前端和要持久化的内容
                String output = String.format("\n\n%s\n\n", result);
                chatHistoryStringBuilder.append(output);
                return output;
            }
            default -> {
                log.error("不支持的消息类型: {}", typeEnum);
                return "";
            }
        }
    }

    /**
     * 构建并部署 Vue 项目
     * 1. 执行 npm install 和 npm run build
     * 2. 将 dist 目录复制到部署目录
     * 3. 更新应用的 deployKey
     */
    private void buildAndDeployVueProject(long appId) {
        // 使用虚拟线程异步执行构建和部署
        Thread.startVirtualThread(() -> {
            try {
                String projectPath = AppConstant.CODE_OUTPUT_ROOT_DIR + "/vue_project_" + appId;
                log.info("开始构建 Vue 项目: {}", projectPath);

                // 1. 执行构建
                boolean buildSuccess = vueProjectBuilder.buildProject(projectPath);
                if (!buildSuccess) {
                    log.error("Vue 项目构建失败: {}", projectPath);
                    return;
                }

                // 2. 检查 dist 目录
                File distDir = new File(projectPath, "dist");
                if (!distDir.exists()) {
                    log.error("dist 目录不存在: {}", distDir.getAbsolutePath());
                    return;
                }

                // 3. 生成 deployKey
                App app = appMapper.selectOneById(appId);
                String deployKey = app.getDeployKey();
                if (StrUtil.isBlank(deployKey)) {
                    deployKey = RandomUtil.randomString(6);
                }

                // 4. 复制 dist 目录到部署目录
                String deployDirPath = AppConstant.CODE_DEPLOY_ROOT_DIR + File.separator + deployKey;
                FileUtil.copyContent(distDir, new File(deployDirPath), true);
                log.info("Vue 项目部署成功，deployKey: {}", deployKey);

                // 5. 更新数据库
                App updateApp = new App();
                updateApp.setId(appId);
                updateApp.setDeployKey(deployKey);
                updateApp.setDeployedTime(LocalDateTime.now());
                appMapper.update(updateApp);

                log.info("Vue 项目构建部署完成，appId: {}, deployKey: {}", appId, deployKey);
            } catch (Exception e) {
                log.error("Vue 项目构建部署异常: {}", e.getMessage(), e);
            }
        });
    }
}
