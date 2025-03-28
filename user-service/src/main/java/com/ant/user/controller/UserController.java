package com.ant.user.controller;

import com.ant.user.model.dto.UserLoginDTO;
import com.ant.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void register(@RequestParam("username") String username,
                         @RequestParam("password") String password) {
        userService.insertUser(username, password);
    }

    @PostMapping("/login")
    public UserLoginDTO login(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        return userService.login(username, password);
    }
}
