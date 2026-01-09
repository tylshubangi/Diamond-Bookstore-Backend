package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.LoginRequest;
import com.bookstore.onlinebookstore.dto.RegisterRequest;
import com.bookstore.onlinebookstore.service.AuthService;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
