package com.ant.user.service;

import com.ant.user.client.AuthServiceClient;
import com.ant.user.exception.UserLoginException;
import com.ant.user.mapper.UserMapper;
import com.ant.user.model.dto.UserLoginDTO;
import com.ant.user.model.entity.JwtDO;
import com.ant.user.model.entity.UserDO;
import com.ant.user.util.BPwdEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthServiceClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new User(
                user.getUsername(),
                user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    public void insertUser(String username, String password) {
        UserDO user = new UserDO();
        user.setUsername(username);
        user.setPassword(BPwdEncoderUtil.BCryptPassword(password));
        userMapper.insertUser(user);
    }

    public UserLoginDTO login(String username, String password) {
        UserDO userDO = userMapper.findByUsername(username);
        if (userDO == null) {
            throw new UserLoginException("用户不存在");
        }
        if (!BPwdEncoderUtil.matches(password, userDO.getPassword())) {
            throw new UserLoginException("密码错误");
        }
        // 获取token
        JwtDO jwt = client.getToken("Basic dWFhLXNlcnZpY2U6MTIzNDU2",
                "password", username, password);
        // 获得用户菜单
        if (jwt == null) {
            throw new UserLoginException("服务异常");
        }
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setJwt(jwt);
        return userLoginDTO;
    }

}
