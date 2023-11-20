package com.pri.petcationbackend.web.dto;

import com.pri.petcationbackend.model.Hotel;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
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
    private List<Reservation> reservations;
    private List<Room> rooms;
    private Map<LocalDate, List<Room>> freeCalendar;
    private Map<LocalDate, List<Room>> takenCalendar;
    private byte[] image;
    private String description;

    public HotelDto(Hotel hotel) {
        this.id = hotel.getHotelId();
        this.name = hotel.getName();
        this.description = hotel.getDescription();
    }
}
