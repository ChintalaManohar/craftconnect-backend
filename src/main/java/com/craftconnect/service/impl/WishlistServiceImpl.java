package com.craftconnect.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.craftconnect.dto.WishlistResponse;
import com.craftconnect.entity.Product;
import com.craftconnect.entity.User;
import com.craftconnect.entity.Wishlist;
import com.craftconnect.repository.ProductRepository;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.repository.WishlistRepository;
import com.craftconnect.service.WishlistService;

@Service
public class WishlistServiceImpl
        implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistServiceImpl(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();
        
        

        System.out.println("Authentication = " + authentication);
        System.out.println("Name = " + authentication.getName());
        System.out.println("Authorities = " + authentication.getAuthorities());

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User Not Found"));
    }

    @Override
    public String addToWishlist(
            Long productId) {

        User buyer = getCurrentUser();

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product Not Found"));

        if (wishlistRepository
                .findByBuyerIdAndProductId(
                        buyer.getId(),
                        productId)
                .isPresent()) {

            throw new RuntimeException(
                    "Product already in wishlist");
        }

        Wishlist wishlist =
                new Wishlist();

        wishlist.setBuyer(buyer);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);

        return "Product added to wishlist";
    }

    @Override
    public String removeFromWishlist(
            Long productId) {

        User buyer = getCurrentUser();

        Wishlist wishlist =
                wishlistRepository
                        .findByBuyerIdAndProductId(
                                buyer.getId(),
                                productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found in wishlist"));

        wishlistRepository.delete(wishlist);

        return "Product removed from wishlist";
    }

    @Override
    public List<WishlistResponse> getWishlist() {

        User buyer = getCurrentUser();

        return wishlistRepository
                .findByBuyerId(buyer.getId())
                .stream()
                .map(w -> new WishlistResponse(
                        w.getProduct().getId(),
                        w.getProduct().getName(),
                        w.getProduct().getDescription(),
                        w.getProduct().getPrice(),
                        w.getProduct().getImageUrl(),
                        w.getProduct().getArtisan().getFullName(),
                        w.getProduct().getCategory().getName()
                ))
                .toList();
    }
}