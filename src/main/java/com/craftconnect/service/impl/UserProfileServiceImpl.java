package com.craftconnect.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.craftconnect.dto.UserProfileResponse;
import com.craftconnect.dto.UserProfileUpdateRequest;
import com.craftconnect.entity.User;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.service.UserProfileService;

@Service
public class UserProfileServiceImpl
        implements UserProfileService {

    private final UserRepository userRepository;

    public UserProfileServiceImpl(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileResponse getMyProfile() {

        User user = getCurrentUser();

        return mapToResponse(user);
    }

    @Override
    public UserProfileResponse updateMyProfile(
            UserProfileUpdateRequest request) {

        User user = getCurrentUser();

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        User savedUser =
                userRepository.save(user);

        return mapToResponse(savedUser);
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));
    }

    private UserProfileResponse mapToResponse(
            User user) {

        UserProfileResponse response =
                new UserProfileResponse();

        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        response.setAvatar(user.getAvatar());

        return response;
    }
}