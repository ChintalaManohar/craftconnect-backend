package com.craftconnect.service;

import java.util.List;

import com.craftconnect.dto.AddressRequest;
import com.craftconnect.dto.AddressResponse;

public interface AddressService {

    AddressResponse addAddress(AddressRequest request);

    List<AddressResponse> getMyAddresses();

    AddressResponse updateAddress(
            Long addressId,
            AddressRequest request);

    String deleteAddress(Long addressId);

    String setDefaultAddress(Long addressId);
}