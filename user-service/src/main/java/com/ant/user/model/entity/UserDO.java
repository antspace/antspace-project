package com.ant.user.model.entity;

import lombok.Data;

@Data
public class UserDO {
    private Long id;
    private String username;
    private String password;
    private String role;
}