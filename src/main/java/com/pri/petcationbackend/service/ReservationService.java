package com.pri.petcationbackend.service;

import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;
import com.pri.petcationbackend.web.dto.ReservationResponseDto;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    void addReservation(List<Room> availableRooms, ReservationRequestDto reservationRequestDto);

    void deleteReservation(Long reservationId);

    Optional<Reservation> findById(Long reservationId);

    ReservationResponseDto getReservationById(Long reservationId);

    void rejectReservation(Reservation reservation);

    List<ReservationResponseDto> getCurrentReservationsForUser();

    List<ReservationResponseDto> getAllReservationsForUser();

    void acceptReservation(Reservation reservation, long availableRoomsSize);

    List<ReservationResponseDto> getConflictedReservations(Reservation reservation, long availableRoomsSize);
    void save(Reservation reservation);

    boolean isReservationCompleted(Long id);
}
