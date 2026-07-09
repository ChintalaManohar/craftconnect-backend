package com.craftconnect.service;

import com.craftconnect.dto.ArtisanProfileResponse;
import com.craftconnect.dto.ArtisanStoryRequest;

public interface ArtisanProfileService {

    String updateProfile(
            ArtisanStoryRequest request);

    ArtisanProfileResponse getArtisanProfile(
            Long artisanId);
    
    ArtisanProfileResponse getMyProfile();
}