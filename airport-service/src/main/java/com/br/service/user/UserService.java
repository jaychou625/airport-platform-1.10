package com.br.service.user;

import com.br.entity.core.User;
import com.br.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务
 * @Author zyx
 * @Date 2019 03 01
 */
@Service("userService")
public class UserService {

    // 用户 Mapper
    @Resource
    private UserMapper userMapper;

    /**
     * 单个用户查询
     * @return
     */
    public User find(Integer userSeq, String userName) {
        return this.userMapper.find(userSeq, userName);
    }

}
