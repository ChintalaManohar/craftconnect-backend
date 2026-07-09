package com.craftconnect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craftconnect.dto.WishlistResponse;
import com.craftconnect.entity.Wishlist;

public interface WishlistRepository
        extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByBuyerId(Long buyerId);

    Optional<Wishlist>
    findByBuyerIdAndProductId(
            Long buyerId,
            Long productId);
}