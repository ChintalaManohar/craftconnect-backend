package com.craftconnect.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.craftconnect.dto.AddToCartRequest;
import com.craftconnect.dto.CartItemResponse;
import com.craftconnect.entity.Cart;
import com.craftconnect.entity.CartItem;
import com.craftconnect.entity.Product;
import com.craftconnect.entity.User;
import com.craftconnect.repository.CartItemRepository;
import com.craftconnect.repository.CartRepository;
import com.craftconnect.repository.ProductRepository;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    
    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));
    }

    public CartServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String addToCart(AddToCartRequest request) {

    	Authentication authentication =
    	        SecurityContextHolder
    	                .getContext()
    	                .getAuthentication();

    	String email = authentication.getName();

    	User buyer = userRepository
    	        .findByEmail(email)
    	        .orElseThrow(() ->
    	                new RuntimeException("User Not Found"));

        Product product = productRepository.findById(
                request.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product Not Found"));

        Cart cart = cartRepository.findByBuyerId(
                buyer.getId())
                .orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setBuyer(buyer);

                    return cartRepository.save(newCart);
                });

        CartItem existingItem =
                cartItemRepository
                        .findByCartIdAndProductId(
                                cart.getId(),
                                product.getId())
                        .orElse(null);

        if(existingItem != null){

            existingItem.setQuantity(
                    existingItem.getQuantity()
                    + request.getQuantity());

            cartItemRepository.save(existingItem);

        }else{

            CartItem item = new CartItem();

            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());

            cartItemRepository.save(item);
        }

        return "Product Added To Cart";
    }

    @Override
    public List<CartItemResponse> getCart() {

        User buyer = getCurrentUser();

        Cart cart = cartRepository
                .findByBuyerId(buyer.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart Not Found"));

        return cartItemRepository
                .findByCartId(cart.getId())
                .stream()
                .map(item ->
                new CartItemResponse(
                		item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getArtisan().getFullName(),
                        item.getProduct().getQuantity()
                	))
                .toList();
    }

    @Override
    public void removeCartItem(Long cartItemId) {

        cartItemRepository.deleteById(cartItemId);
    }
    @Override
    public void updateQuantity(
            Long cartItemId,
            Integer quantity) {

        CartItem item =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart Item Not Found"));

        item.setQuantity(quantity);

        cartItemRepository.save(item);
    }
}