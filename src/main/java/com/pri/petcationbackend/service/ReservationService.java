package com.pri.petcationbackend.service;

import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;

import java.util.List;

public interface ReservationService {
    void addReservation(List<Room> availableRooms, ReservationRequestDto reservationRequestDto);
}
