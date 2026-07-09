package com.craftconnect.service;

import java.util.List;

import com.craftconnect.dto.ArtisanOrderResponse;

public interface ArtisanOrderService {

    List<ArtisanOrderResponse> getArtisanOrders();

    String shipOrder(Long orderId);
}