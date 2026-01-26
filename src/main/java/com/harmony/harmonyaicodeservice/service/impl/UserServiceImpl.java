package com.harmony.harmonyaicodeservice.service.impl;

import com.harmony.harmonyaicodeservice.model.entity.User;
import com.harmony.harmonyaicodeservice.mapper.UserMapper;
import com.harmony.harmonyaicodeservice.service.UserService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author <a href="https://gitee.com/Harmony_TL">harmony</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
