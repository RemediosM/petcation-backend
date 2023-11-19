package com.pri.petcationbackend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressDto {

    private String phoneNumber;
    private String street;
    private String city;
    private String country;
}
