package com.ant.user.model.dto;

import com.ant.user.model.entity.JwtDO;
import lombok.Data;

@Data
public class UserLoginDTO {
    private String username;
    private String password;
    private JwtDO jwt;
}
