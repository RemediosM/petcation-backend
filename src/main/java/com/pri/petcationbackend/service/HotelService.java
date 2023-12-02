package com.pri.petcationbackend.service;

import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.web.dto.HotelDetailsDto;
import com.pri.petcationbackend.web.dto.HotelDto;
import com.pri.petcationbackend.web.dto.HotelRequestDto;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;

import java.util.List;

public interface HotelService {
    List<HotelDto> getAllHotels();
    List<HotelDto> getHotels(HotelRequestDto hotelRequestDto);

    HotelDetailsDto getHotelById(Long id);

    List<Room> checkAvailableRooms(ReservationRequestDto reservationDto);
}
