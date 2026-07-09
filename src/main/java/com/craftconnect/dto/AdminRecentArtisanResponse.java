package com.craftconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminRecentArtisanResponse {

    private Long id;

    private String name;

    private String category;

    private String village;

    private String avatar;

    private Boolean isComplete;

}