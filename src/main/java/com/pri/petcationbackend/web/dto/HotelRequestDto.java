package com.pri.petcationbackend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelRequestDto {

    private LocalDate from;
    private LocalDate to;
    private Integer maxDistance;
    private Double lat;
    private Double lon;
    private List<PetTypeQtyDto> petTypeQtyDtoList;
}
