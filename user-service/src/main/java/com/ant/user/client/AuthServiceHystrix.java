package com.ant.user.client;

import com.ant.user.model.entity.JwtDO;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceHystrix implements AuthServiceClient {
    @Override
    public JwtDO getToken(String authorization, String type, String username, String password) {
        return null;
    }
}