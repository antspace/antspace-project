package com.ant.user.mapper;

import com.ant.user.model.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insertUser(UserDO user);
}