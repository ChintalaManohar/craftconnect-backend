package com.craftconnect.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private String imageUrl;

    
    
    private String category;

    private String artisanName;
    
    private Long artisanId;

    private LocalDateTime createdAt;
    
    private List<ProductImageResponse> images; 
}