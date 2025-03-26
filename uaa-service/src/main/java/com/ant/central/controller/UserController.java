package com.ant.central.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/getUserList")
    public ResponseEntity<?> getUserList() {
        return ResponseEntity.ok("getUserList");
    }
}
