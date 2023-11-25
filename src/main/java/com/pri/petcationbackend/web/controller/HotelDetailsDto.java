package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.web.dto.AddressDto;
import com.pri.petcationbackend.web.dto.PetTypeQtyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelDetailsDto {

    private Long id;
    private String name;
    private String description;
    private AddressDto addressDto;
    private Boolean isAnyReservation;
    List<PetTypeQtyDto> allAvailableRoomsByPetType;
    Map<LocalDate, List<PetTypeQtyDto>> freeDatesList;
}
