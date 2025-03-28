package com.ant.user.client;

import com.ant.user.model.entity.JwtDO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "uaa-service", fallback = AuthServiceHystrix.class)
public interface AuthServiceClient {

    @PostMapping(value = "/oauth/token")
    JwtDO getToken(@RequestHeader(value = "Authorization") String authorization, @RequestParam("grant_type") String type,
                   @RequestParam("username") String username, @RequestParam("password") String password);

}
