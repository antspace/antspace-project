package com.ant.central.controller;

import com.ant.central.model.dto.AuthDTO;
import com.ant.central.model.dto.RegisterDTO;
import com.ant.central.model.entity.UserDO;
import com.ant.central.service.AuthService;
import com.ant.central.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDto) {
        UserDO user = new UserDO();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userDetailsService.registerUser(user);
        return ResponseEntity.ok("用户注册成功");
    }

    @PostMapping("/token")
    public ResponseEntity<String> createToken(@RequestBody AuthDTO authDto) throws Exception {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("用户名或密码错误", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authDto.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }
}
