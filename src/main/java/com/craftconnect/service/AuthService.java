package com.craftconnect.service;

import com.craftconnect.dto.LoginRequest;
import com.craftconnect.dto.LoginResponse;
import com.craftconnect.dto.RegisterRequest;
import com.craftconnect.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}