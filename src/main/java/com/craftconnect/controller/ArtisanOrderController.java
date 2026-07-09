package com.craftconnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.ArtisanOrderResponse;
import com.craftconnect.service.ArtisanOrderService;

@RestController
@RequestMapping("/api/artisan/orders")
public class ArtisanOrderController {

    private final ArtisanOrderService artisanOrderService;

    public ArtisanOrderController(
            ArtisanOrderService artisanOrderService) {

        this.artisanOrderService =
                artisanOrderService;
    }

    @GetMapping
    public List<ArtisanOrderResponse>
    getArtisanOrders() {

        return artisanOrderService
                .getArtisanOrders();
    }

    @PutMapping("/{orderId}/ship")
    public String shipOrder(
            @PathVariable Long orderId) {

        return artisanOrderService
                .shipOrder(orderId);
    }
}