package com.pri.petcationbackend.web.dto;

import com.pri.petcationbackend.model.Hotel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRoomDto {

    private Long id;
    private String name;
    private String description;

    public HotelRoomDto(Hotel hotel) {
        this.id = hotel.getHotelId();
        this.name = hotel.getName();
        this.description = hotel.getDescription();
    }
}
