package com.pri.petcationbackend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private Long id;
    private BigDecimal price;
    private HotelRoomDto hotelDto;
    private PetTypeEnum petType;
}
