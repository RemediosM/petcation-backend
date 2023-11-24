package com.pri.petcationbackend.service;

import com.pri.petcationbackend.web.dto.ReservationRequestDto;

public interface ReservationService {
    void addReservation(ReservationRequestDto reservationRequestDto);
}
