package com.craftconnect.dto;

import com.craftconnect.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;

    private String village;
    private String district;
    private String state;
    private String pincode;
    private String category;

    private String avatar;
    private String coverImage;

    private String storyTitle;
    private String storyDescription;
    
    private boolean active;
}