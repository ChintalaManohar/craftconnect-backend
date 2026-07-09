package com.craftconnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.AddToCartRequest;
import com.craftconnect.dto.CartItemResponse;
import com.craftconnect.dto.UpdateCartQuantityRequest;
import com.craftconnect.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(
            CartService cartService) {

        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestBody AddToCartRequest request) {

        return cartService.addToCart(request);
    }

    @GetMapping
    public List<CartItemResponse> getCart() {

        return cartService.getCart();
    }
    @DeleteMapping("/item/{cartItemId}")
    public String removeCartItem(
            @PathVariable Long cartItemId) {

        cartService.removeCartItem(cartItemId);

        return "Item Removed";
    }
    @PutMapping("/item/{cartItemId}")
    public String updateQuantity(
            @PathVariable Long cartItemId,
            @RequestBody UpdateCartQuantityRequest request) {

        cartService.updateQuantity(
                cartItemId,
                request.getQuantity());

        return "Quantity Updated";
    }
}