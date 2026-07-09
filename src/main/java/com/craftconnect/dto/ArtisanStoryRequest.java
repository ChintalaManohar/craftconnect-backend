package com.craftconnect.dto;

import lombok.Data;

@Data
public class ArtisanStoryRequest {

    private String name;

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