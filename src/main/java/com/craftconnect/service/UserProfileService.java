package com.craftconnect.service;

import com.craftconnect.dto.UserProfileResponse;
import com.craftconnect.dto.UserProfileUpdateRequest;

public interface UserProfileService {

    UserProfileResponse getMyProfile();

    UserProfileResponse updateMyProfile(
            UserProfileUpdateRequest request);
}