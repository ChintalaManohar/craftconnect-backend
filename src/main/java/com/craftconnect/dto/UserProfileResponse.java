package com.craftconnect.dto;

import com.craftconnect.entity.Role;
import lombok.Data;

@Data
public class UserProfileResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private String avatar;
}