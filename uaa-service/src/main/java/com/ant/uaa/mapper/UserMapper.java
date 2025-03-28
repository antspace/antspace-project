package com.ant.uaa.mapper;

import com.ant.uaa.model.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDO findByUsername(String username);

}