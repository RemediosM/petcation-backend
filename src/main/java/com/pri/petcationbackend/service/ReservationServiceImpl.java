package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.PetRepository;
import com.pri.petcationbackend.dao.ReservationRepository;
import com.pri.petcationbackend.dao.RoomRepository;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final PetRepository petRepository;

    @Override
    public void addReservation(List<Room> availableRooms, ReservationRequestDto reservationRequestDto) {
//        Integer max = reservationRepository.findMaxReservationNumber();
//        Long reservationNo = max != null ? Long.valueOf(++max) : 1;
//        List<Reservation> reservationsToSave = new ArrayList<>();
//        reservationRequestDto.getPetIds().forEach(petId -> {
//            Room room = roomRepository.findById(petRoomDto.getRoomId()).orElse(null);
//            Pet pet = petRepository.findById(petId).orElse(null);
//            Reservation reservation = Reservation.builder()
//                    .reservationNo(reservationNo)
//                    .from(reservationRequestDto.getFrom())
//                    .to(reservationRequestDto.getTo())
//                    .isTrial(false)
//                    .pet(pet)
//                    .room(room)
//                    .build();
//            reservationsToSave.add(reservation);
//
//        });
//        reservationRepository.saveAll(reservationsToSave);
    }
}
