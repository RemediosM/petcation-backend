package com.pri.petcationbackend.web.dto;

import com.pri.petcationbackend.model.Hotel;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDto {

    private Long id;
    private String name;
    private List<ReservationResponseDto> reservations;
    private List<RoomDto> rooms;
    private Map<LocalDate, List<RoomDto>> freeCalendar;
    private Map<LocalDate, List<RoomDto>> takenCalendar;
    private List<String> images;
    private AddressDto addressDto;
    private String description;

    public HotelDto(Hotel hotel) {
        this.id = hotel.getHotelId();
        this.name = hotel.getName();
        this.description = hotel.getDescription();
    }
}
