package com.craftconnect.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.craftconnect.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ArtisanOrderResponse {

    private Long orderId;

    private String buyerName;

    private String productName;

    private String imageUrl;

    private Integer quantity;

    private BigDecimal price;

    private OrderStatus status;

    private LocalDateTime createdAt;
}