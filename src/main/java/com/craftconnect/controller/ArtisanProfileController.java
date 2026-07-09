package com.craftconnect.controller;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.ArtisanProfileResponse;
import com.craftconnect.dto.ArtisanStoryRequest;
import com.craftconnect.service.ArtisanProfileService;

@RestController
@RequestMapping("/api/artisan")
public class ArtisanProfileController {

    private final ArtisanProfileService
            artisanProfileService;

    public ArtisanProfileController(
            ArtisanProfileService artisanProfileService) {

        this.artisanProfileService =
                artisanProfileService;
    }

    @PutMapping("/profile")
    public String updateProfile(
            @RequestBody
            ArtisanStoryRequest request) {

        return artisanProfileService
                .updateProfile(request);
    }

    @GetMapping("/{artisanId}")
    public ArtisanProfileResponse
    getArtisanProfile(
            @PathVariable Long artisanId) {

        return artisanProfileService
                .getArtisanProfile(
                        artisanId);
    }
    @GetMapping("/profile")
    public ArtisanProfileResponse getMyProfile() {

        return artisanProfileService.getMyProfile();
    }
}