package com.ant.user.service;

import com.ant.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void registerUser(UserDO user) {
        user.setRole("ROLE_USER"); // 默认角色
        userMapper.insertUser(user);
    }
}
