package com.craftconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArtisanProfileResponse {

    private Long artisanId;

    private String artisanName;

    private String phone;

    private String village;

    private String district;

    private String state;

    private String pincode;

    private String category;

    private String coverImage;

    private String avatar;

    private String storyTitle;

    private String storyDescription;
}