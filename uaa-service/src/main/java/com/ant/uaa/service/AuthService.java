package com.ant.uaa.service;

import com.ant.uaa.mapper.UserMapper;
import com.ant.uaa.model.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Arrays;


@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        user.setRole("ROLE_USER");
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
