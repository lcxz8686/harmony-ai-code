package com.harmony.harmonyaicodeservice.core.parser;

/**
 * 代码解析器策略接口
 *
 * @author: <a href="https://gitee.com/Harmony_TL">harmony</a>
 * @DateTime: 2026/2/2 14:15
 */
public interface CodeParser<T> {

    /**
     * 解析代码内容
     *
     * @param codeContent 原始代码内容
     * @return 解析后的结果对象
     */
    T parseCode(String codeContent);
}
