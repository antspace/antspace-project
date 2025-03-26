package com.ant.central.mapper;

import com.ant.central.model.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDO findByUsername(String username);

    void insertUser(UserDO user);
}