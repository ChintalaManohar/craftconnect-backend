package com.craftconnect.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminRecentOrderResponse {

    private Long id;

    private String customerName;

    private String productName;

    private Integer quantity;

    private BigDecimal price;

    private String status;

}