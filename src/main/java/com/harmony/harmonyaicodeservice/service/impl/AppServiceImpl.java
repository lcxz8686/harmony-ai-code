package com.harmony.harmonyaicodeservice.service.impl;


import com.harmony.harmonyaicodeservice.mapper.AppMapper;
import com.harmony.harmonyaicodeservice.model.entity.App;
import com.harmony.harmonyaicodeservice.service.AppService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author <a href="https://gitee.com/Harmony_TL">harmony</a>
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService {

}
