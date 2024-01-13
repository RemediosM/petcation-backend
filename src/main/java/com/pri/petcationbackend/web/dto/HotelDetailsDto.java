package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HotelDetailsDto {

    private Long id;
    private String name;
    private String description;
    private AddressDto addressDto;
    private Boolean isAnyReservation;
    private BigDecimal averageRate;
    private List<HotelRateDto> rates;
    List<PetTypeQtyDto> allAvailableRoomsByPetType;
    Map<LocalDate, List<PetTypeQtyDto>> freeDatesList;
    private List<HotelsImageDto> images;
}
