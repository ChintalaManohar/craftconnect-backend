package com.craftconnect.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String phoneNumber;

    private String houseNo;

    private String area;

    private String city;

    private String state;

    private String pincode;

    private String landmark;

    private String addressType; // HOME / WORK / OTHER

    private Boolean defaultAddress = false;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;
}