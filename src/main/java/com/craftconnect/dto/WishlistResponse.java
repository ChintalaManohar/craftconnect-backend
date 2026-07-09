package com.craftconnect.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {

    private Long productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String artisanName;
    private String category;
}