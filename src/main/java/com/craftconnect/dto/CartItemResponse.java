package com.craftconnect.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {

    private Long cartItemId;

    private Long productId;

    private String productName;

    private BigDecimal price;

    // Quantity selected by buyer
    private Integer quantity;

    private String imageUrl;

    private String artisanName;

    // Current product stock
    private Integer availableStock;
}