package com.pri.petcationbackend.web.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class AddressDto {

    private String phoneNumber;
    private String street;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
}
