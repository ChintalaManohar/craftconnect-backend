package com.craftconnect.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminRecentProductResponse {

    private Long id;

    private String name;

    private String artisan;

    private BigDecimal price;

    private String image;

}