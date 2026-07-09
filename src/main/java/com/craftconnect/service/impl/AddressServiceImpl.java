package com.craftconnect.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.craftconnect.dto.AddressRequest;
import com.craftconnect.dto.AddressResponse;
import com.craftconnect.entity.Address;
import com.craftconnect.entity.User;
import com.craftconnect.repository.AddressRepository;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            UserRepository userRepository) {

        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

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

    @Override
    public AddressResponse addAddress(AddressRequest request) {

        User buyer = getCurrentUser();

        if (Boolean.TRUE.equals(request.getDefaultAddress())) {

            addressRepository
                    .findByBuyerId(buyer.getId())
                    .forEach(address -> {
                        address.setDefaultAddress(false);
                        addressRepository.save(address);
                    });
        }

        Address address = new Address();

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setHouseNo(request.getHouseNo());
        address.setArea(request.getArea());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLandmark(request.getLandmark());
        address.setAddressType(request.getAddressType());
        address.setDefaultAddress(request.getDefaultAddress());
        address.setBuyer(buyer);

        Address saved = addressRepository.save(address);

        return new AddressResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getPhoneNumber(),
                saved.getHouseNo(),
                saved.getArea(),
                saved.getCity(),
                saved.getState(),
                saved.getPincode(),
                saved.getLandmark(),
                saved.getAddressType(),
                saved.getDefaultAddress());
    }

    @Override
    public List<AddressResponse> getMyAddresses() {

        User buyer = getCurrentUser();

        return addressRepository
                .findByBuyerId(buyer.getId())
                .stream()
                .map(address -> new AddressResponse(
                        address.getId(),
                        address.getFullName(),
                        address.getPhoneNumber(),
                        address.getHouseNo(),
                        address.getArea(),
                        address.getCity(),
                        address.getState(),
                        address.getPincode(),
                        address.getLandmark(),
                        address.getAddressType(),
                        address.getDefaultAddress()))
                .toList();
    }

    @Override
    public AddressResponse updateAddress(
            Long addressId,
            AddressRequest request) {

        User buyer = getCurrentUser();

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new RuntimeException("Address Not Found"));

        if (!address.getBuyer().getId().equals(buyer.getId())) {

            throw new RuntimeException(
                    "You can update only your own addresses");
        }

        if (Boolean.TRUE.equals(request.getDefaultAddress())) {

            addressRepository
                    .findByBuyerId(buyer.getId())
                    .forEach(a -> {
                        a.setDefaultAddress(false);
                        addressRepository.save(a);
                    });
        }

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setHouseNo(request.getHouseNo());
        address.setArea(request.getArea());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLandmark(request.getLandmark());
        address.setAddressType(request.getAddressType());
        address.setDefaultAddress(request.getDefaultAddress());

        Address updated = addressRepository.save(address);

        return new AddressResponse(
                updated.getId(),
                updated.getFullName(),
                updated.getPhoneNumber(),
                updated.getHouseNo(),
                updated.getArea(),
                updated.getCity(),
                updated.getState(),
                updated.getPincode(),
                updated.getLandmark(),
                updated.getAddressType(),
                updated.getDefaultAddress());
    }

    @Override
    public String deleteAddress(Long addressId) {

        User buyer = getCurrentUser();

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new RuntimeException("Address Not Found"));

        if (!address.getBuyer().getId().equals(buyer.getId())) {

            throw new RuntimeException(
                    "You can delete only your own address");
        }

        addressRepository.delete(address);

        return "Address Deleted Successfully";
    }

    @Override
    public String setDefaultAddress(Long addressId) {

        User buyer = getCurrentUser();

        addressRepository
                .findByBuyerId(buyer.getId())
                .forEach(address -> {
                    address.setDefaultAddress(false);
                    addressRepository.save(address);
                });

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new RuntimeException("Address Not Found"));

        address.setDefaultAddress(true);

        addressRepository.save(address);

        return "Default Address Updated";
    }
}