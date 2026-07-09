package com.craftconnect.dto;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {

    private String fullName;
    private String phone;
    private String avatar;
}