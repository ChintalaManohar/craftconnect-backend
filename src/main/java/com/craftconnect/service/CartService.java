package com.craftconnect.service;

import java.util.List;

import com.craftconnect.dto.AddToCartRequest;
import com.craftconnect.dto.CartItemResponse;

public interface CartService {

    String addToCart(AddToCartRequest request);

    List<CartItemResponse> getCart();
    
    
    void updateQuantity(
            Long cartItemId,
            Integer quantity);

    void removeCartItem(Long cartItemId);
}