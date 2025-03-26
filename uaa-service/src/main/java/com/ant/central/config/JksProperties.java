package com.ant.central.config;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Component
@ConfigurationProperties(prefix = "security.jwt.keystore")
public class JksProperties {
    private String path;
    private String password;
    private String keyAlias;
    private String keyPassword;
}
