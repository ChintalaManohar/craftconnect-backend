package com.craftconnect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craftconnect.entity.Address;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByBuyerId(Long buyerId);

    Optional<Address> findByBuyerIdAndDefaultAddressTrue(Long buyerId);
}