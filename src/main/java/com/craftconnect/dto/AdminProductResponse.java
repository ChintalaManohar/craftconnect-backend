package com.craftconnect.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private boolean active;

    private String imageUrl;

    private LocalDateTime createdAt;

    private Long categoryId;

    private String categoryName;

    private Long artisanId;

    private String artisanName;
}