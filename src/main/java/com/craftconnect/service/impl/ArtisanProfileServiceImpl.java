package com.craftconnect.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.craftconnect.dto.ArtisanProfileResponse;
import com.craftconnect.dto.ArtisanStoryRequest;
import com.craftconnect.entity.Role;
import com.craftconnect.entity.User;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.service.ArtisanProfileService;

@Service
public class ArtisanProfileServiceImpl
        implements ArtisanProfileService {

    private final UserRepository userRepository;

    public ArtisanProfileServiceImpl(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User Not Found"));
    }
    private ArtisanProfileResponse mapToResponse(User artisan) {

        return new ArtisanProfileResponse(
                artisan.getId(),
                artisan.getFullName(),
                artisan.getPhone(),
                artisan.getVillage(),
                artisan.getDistrict(),
                artisan.getState(),
                artisan.getPincode(),
                artisan.getCategory(),
                artisan.getCoverImage(),
                artisan.getAvatar(),
                artisan.getStoryTitle(),
                artisan.getStoryDescription()
        );
    }

    @Override
    public String updateProfile(ArtisanStoryRequest request) {

        User artisan = getCurrentUser();

        artisan.setFullName(request.getName());
        artisan.setPhone(request.getPhone());
        artisan.setVillage(request.getVillage());
        artisan.setDistrict(request.getDistrict());
        artisan.setState(request.getState());
        artisan.setPincode(request.getPincode());
        artisan.setCategory(request.getCategory());
        artisan.setCoverImage(request.getCoverImage());
        artisan.setAvatar(request.getAvatar());
        artisan.setStoryTitle(request.getStoryTitle());
        artisan.setStoryDescription(request.getStoryDescription());

        userRepository.save(artisan);

        return "Profile Updated Successfully";
    }
    @Override
    public ArtisanProfileResponse getArtisanProfile(Long artisanId) {

        User artisan = userRepository.findById(artisanId)
                .orElseThrow(() ->
                        new RuntimeException("Artisan Not Found"));

        if (artisan.getRole() != Role.ARTISAN) {
            throw new RuntimeException("User is not an artisan");
        }

        return mapToResponse(artisan);
    }


    @Override
    public ArtisanProfileResponse getMyProfile() {

        User artisan = getCurrentUser();

        if (artisan.getRole() != Role.ARTISAN) {
            throw new RuntimeException("User is not an artisan");
        }

        return mapToResponse(artisan);
    }
}