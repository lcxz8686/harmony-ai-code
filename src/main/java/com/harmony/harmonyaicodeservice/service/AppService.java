package com.harmony.harmonyaicodeservice.service;

import com.harmony.harmonyaicodeservice.model.dto.AppQueryRequest;
import com.harmony.harmonyaicodeservice.model.entity.App;
import com.harmony.harmonyaicodeservice.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://gitee.com/Harmony_TL">harmony</a>
 */
public interface AppService extends IService<App> {

    /**
     * 获取脱敏后的应用信息
     *
     * @param app 应用信息
     * @return
     */
    AppVO getAppVO(App app);

    /**
     * 获取脱敏后的应用信息（列表）
     *
     * @param appList 应用列表
     * @return
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 根据查询条件构造数据查询参数
     *
     * @param appQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 验证应用是否属于当前用户
     *
     * @param appId 应用id
     * @param request 请求对象
     */
    void validateAppOwner(Long appId, HttpServletRequest request);
}
