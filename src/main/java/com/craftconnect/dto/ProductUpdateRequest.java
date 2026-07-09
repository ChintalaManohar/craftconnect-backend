package com.craftconnect.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProductUpdateRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
    private Long categoryId;
    private List<ProductImageRequest> images;
}