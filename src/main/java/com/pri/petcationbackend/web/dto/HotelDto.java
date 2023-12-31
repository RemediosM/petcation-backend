package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDto {

    private Long id;
    private String name;
    private List<RoomHotelDto> rooms;
    private List<HotelsImageDto> images;
    private AddressDto addressDto;
    private String description;
    private BigDecimal averageRate;
}
