package com.craftconnect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craftconnect.entity.Cart;

public interface CartRepository
        extends JpaRepository<Cart, Long> {

    Optional<Cart> findByBuyerId(Long buyerId);
}