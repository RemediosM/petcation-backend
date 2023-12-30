package com.pri.petcationbackend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class HotelRateDto {

    private BigDecimal rate;
    private String comment;
    private PetOwnerDto petOwnerDto;

}
