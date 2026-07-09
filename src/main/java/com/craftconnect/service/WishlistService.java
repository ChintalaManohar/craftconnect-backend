package com.craftconnect.service;

import java.util.List;

import com.craftconnect.dto.WishlistResponse;
import com.craftconnect.entity.Wishlist;

public interface WishlistService {

    String addToWishlist(Long productId);

    String removeFromWishlist(Long productId);

    List<WishlistResponse> getWishlist();
}