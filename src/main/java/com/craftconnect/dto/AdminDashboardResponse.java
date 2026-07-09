package com.craftconnect.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDashboardResponse {

    private Long totalCustomers;

    private Long totalArtisans;

    private Long totalProducts;

    private Long totalOrders;

    private BigDecimal totalRevenue;

    private Long pendingOrders;

    private List<AdminRecentOrderResponse> recentOrders;

    private List<AdminRecentProductResponse> recentProducts;

    private List<AdminRecentArtisanResponse> recentArtisans;

}