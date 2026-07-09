package com.craftconnect.dto;

import lombok.Data;

@Data
public class AddressRequest {

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