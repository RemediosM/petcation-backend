package com.pri.petcationbackend.service;

import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.web.dto.*;

import java.util.List;

public interface HotelService {
    List<HotelDto> getAllHotels();
    List<HotelDto> getHotels(HotelRequestDto hotelRequestDto);

    HotelDetailsDto getHotelById(Long id);

    List<Room> checkAvailableRooms(ReservationRequestDto reservationDto);

    void addHotelRate(HotelRateRequestDto hotelRateRequestDto, User currentUser);

    RoomDto findRoomByRoomId(Long id);
}
