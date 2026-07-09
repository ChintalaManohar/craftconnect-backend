package com.craftconnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.WishlistResponse;
import com.craftconnect.entity.Wishlist;
import com.craftconnect.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(
            WishlistService wishlistService) {

        this.wishlistService = wishlistService;
    }

    @PostMapping("/{productId}")
    public String addToWishlist(
            @PathVariable Long productId) {

        return wishlistService
                .addToWishlist(productId);
    }

    @DeleteMapping("/{productId}")
    public String removeFromWishlist(
            @PathVariable Long productId) {

        return wishlistService
                .removeFromWishlist(productId);
    }

    @GetMapping
    public List<WishlistResponse> getWishlist() {

    	System.out.println("Wishlist API HIT");
        return wishlistService
                .getWishlist();
    }
}