package com.pri.petcationbackend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationPetDto {

    private PetDto petDto;
    private Boolean isAnyRateForPet;
}
