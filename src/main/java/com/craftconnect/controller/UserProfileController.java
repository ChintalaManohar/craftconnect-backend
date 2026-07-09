package com.craftconnect.controller;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.UserProfileResponse;
import com.craftconnect.dto.UserProfileUpdateRequest;
import com.craftconnect.service.UserProfileService;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(
            UserProfileService userProfileService) {

        this.userProfileService =
                userProfileService;
    }

    @GetMapping
    public UserProfileResponse getMyProfile() {

        return userProfileService.getMyProfile();
    }

    @PutMapping
    public UserProfileResponse updateMyProfile(
            @RequestBody
            UserProfileUpdateRequest request) {

        return userProfileService
                .updateMyProfile(request);
    }
}