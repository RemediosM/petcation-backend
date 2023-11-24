package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.PetRepository;
import com.pri.petcationbackend.dao.ReservationRepository;
import com.pri.petcationbackend.dao.RoomRepository;
import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final PetRepository petRepository;

    @Override
    public void addReservation(ReservationRequestDto reservationRequestDto) {
        Room room = roomRepository.findById(reservationRequestDto.getRoomId()).orElse(null);
        Pet pet = petRepository.findById(reservationRequestDto.getPetId()).orElse(null);
        Integer max = reservationRepository.findMaxReservationNumber();
        Reservation reservation = Reservation.builder()
                .reservationNo(max != null ? Long.valueOf(++max) : 1)
                .from(reservationRequestDto.getFrom())
                .to(reservationRequestDto.getTo())
                .isTrial(reservationRequestDto.getIsTrial())
                .pet(pet)
                .room(room)
                .build();
        reservationRepository.save(reservation);
    }
}
