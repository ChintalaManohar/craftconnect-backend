package com.craftconnect.dto;

import com.craftconnect.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private String fullName;

    private String email;

    private Role role;
}