package com.craftconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressResponse {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String houseNo;

    private String area;

    private String city;

    private String state;

    private String pincode;

    private String landmark;

    private String addressType;

    private Boolean defaultAddress;
}