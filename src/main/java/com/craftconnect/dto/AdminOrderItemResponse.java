package com.craftconnect.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminOrderItemResponse {

    private Long productId;

    private String productName;

    private String imageUrl;

    private Integer quantity;

    private BigDecimal price;

    private Long artisanId;

    private String artisanName;
}