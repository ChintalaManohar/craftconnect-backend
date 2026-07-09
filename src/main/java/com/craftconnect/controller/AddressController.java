package com.craftconnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.AddressRequest;
import com.craftconnect.dto.AddressResponse;
import com.craftconnect.service.AddressService;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public AddressResponse addAddress(
            @RequestBody AddressRequest request) {

        return addressService.addAddress(request);
    }

    @GetMapping
    public List<AddressResponse> getMyAddresses() {

        return addressService.getMyAddresses();
    }

    @PutMapping("/{addressId}")
    public AddressResponse updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequest request) {

        return addressService.updateAddress(
                addressId,
                request);
    }

    @DeleteMapping("/{addressId}")
    public String deleteAddress(
            @PathVariable Long addressId) {

        return addressService.deleteAddress(addressId);
    }

    @PutMapping("/default/{addressId}")
    public String setDefaultAddress(
            @PathVariable Long addressId) {

        return addressService.setDefaultAddress(addressId);
    }
}