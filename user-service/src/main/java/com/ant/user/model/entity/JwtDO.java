package com.ant.user.model.entity;

import lombok.Data;

@Data
public class JwtDO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private String jti;
}
