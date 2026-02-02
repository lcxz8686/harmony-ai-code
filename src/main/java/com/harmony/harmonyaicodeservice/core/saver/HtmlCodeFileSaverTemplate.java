package com.harmony.harmonyaicodeservice.core.saver;

import cn.hutool.core.util.StrUtil;
import com.harmony.harmonyaicodeservice.ai.model.HtmlCodeResult;
import com.harmony.harmonyaicodeservice.exception.BusinessException;
import com.harmony.harmonyaicodeservice.exception.ErrorCode;
import com.harmony.harmonyaicodeservice.model.enums.CodeGenTypeEnum;

/**
 * HTML代码文件保存器
 *
 * @author: <a href="https://gitee.com/Harmony_TL">harmony</a>
 * @DateTime: 2026-02-02
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        // 保存 HTML 文件
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // HTML 代码不能为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码内容不能为空");
        }
    }
}
