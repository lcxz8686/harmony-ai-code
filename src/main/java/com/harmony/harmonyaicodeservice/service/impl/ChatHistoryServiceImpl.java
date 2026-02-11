package com.harmony.harmonyaicodeservice.service.impl;

import com.harmony.harmonyaicodeservice.mapper.ChatHistoryMapper;
import com.harmony.harmonyaicodeservice.model.entity.ChatHistory;
import com.harmony.harmonyaicodeservice.service.ChatHistoryService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 对话历史 服务层实现。
 *
 * @author <a href="https://gitee.com/Harmony_TL">harmony</a>
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService {

}
