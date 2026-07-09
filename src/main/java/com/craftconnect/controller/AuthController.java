package com.craftconnect.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.LoginRequest;
import com.craftconnect.dto.LoginResponse;
import com.craftconnect.dto.RegisterRequest;
import com.craftconnect.dto.RegisterResponse;
import com.craftconnect.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request){

        return authService.register(request);
    }
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }
    
    @GetMapping("/test")
    public String test() {
        return "JWT Working";
    }
    @GetMapping("/me")
    public String currentUser(
            Authentication authentication) {

        return authentication.getName();
    }
}