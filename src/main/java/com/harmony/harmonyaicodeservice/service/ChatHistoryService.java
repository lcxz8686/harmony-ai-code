package com.harmony.harmonyaicodeservice.service;

import com.harmony.harmonyaicodeservice.model.dto.chathistory.ChatHistoryQueryRequest;
import com.harmony.harmonyaicodeservice.model.entity.ChatHistory;
import com.harmony.harmonyaicodeservice.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author <a href="https://gitee.com/Harmony_TL">harmony</a>
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 新增对话历史
     * @param appId
     * @param message
     * @param messageType
     * @param userId
     * @return
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 获取应用下的对话历史
     * @param appId
     * @param pageSize
     * @param lastCreateTime
     * @param loginUser
     * @return
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * 根据应用ID删除对话历史
     * @param appId
     * @return
     */
    boolean deleteByAppId(Long appId);

    /**
     * 加载应用下的对话历史到内存
     * @param appId
     * @param chatMemory
     * @param maxCount
     * @return
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);

    /**
     * 获取查询包装类
     * @param chatHistoryQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);
}
