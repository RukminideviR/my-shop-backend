package com.mouritech.security_frontend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @PostMapping("/testUser")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from User Controller!");
    }
}

