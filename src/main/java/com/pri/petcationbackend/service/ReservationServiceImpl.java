package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.HotelRepository;
import com.pri.petcationbackend.dao.PetRepository;
import com.pri.petcationbackend.dao.ReservationRepository;
import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;
import com.pri.petcationbackend.web.dto.ReservationResponseDto;
import com.pri.petcationbackend.web.dto.ReservationStatusEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final PetRepository petRepository;

    private final HotelRepository hotelRepository;

    @Override
    public void addReservation(List<Room> availableRooms, ReservationRequestDto reservationRequestDto) {
        Integer max = reservationRepository.findMaxReservationNumber();
        Long reservationNo = max != null ? Long.valueOf(++max) : 1;
        if(reservationRequestDto.getPetIds() != null) {
            int petsQty = reservationRequestDto.getPetIds().size();
            List<Pet> pets = petRepository.findAllById(reservationRequestDto.getPetIds());
            if(pets.size() == petsQty) {
                Reservation reservation = Reservation.builder()
                        .reservationNo(reservationNo)
                        .from(reservationRequestDto.getFrom())
                        .to(reservationRequestDto.getTo())
                        .isTrial(false)
                        .pets(pets)
                        .hotel(availableRooms.get(0).getHotel())
                        .status(ReservationStatusEnum.PENDING.getCode())
                        .rooms(availableRooms.stream().limit(petsQty).toList())
                        .petOwner(pets.get(0).getPetOwner())
                        .build();

                reservationRepository.save(reservation);
            }
        }
    }

    @Override
    public void deleteReservation(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        reservation.ifPresent(r -> {
            if(ReservationStatusEnum.PENDING.getCode().equals(r.getStatus())) {
                reservationRepository.delete(r);
            } else {
                r.setStatus(ReservationStatusEnum.DELETED.getCode());
                reservationRepository.save(r);
            }
        });
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        return reservationId != null ? reservationRepository.findById(reservationId) : Optional.empty();
    }

    @Override
    public void rejectReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatusEnum.REJECTED.getCode());
        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationResponseDto> getCurrentReservationsForUser() {
        LocalDate now = LocalDate.now();
        return getAllReservationsForUser().stream()
                .filter(r -> r.getFrom().isAfter(now)
                || r.getTo().isAfter(now)
                || r.getFrom().isEqual(now)
                || r.getTo().isEqual(now))
                .toList();
    }

    @Override
    public List<ReservationResponseDto> getAllReservationsForUser() {
        User user = userService.getCurrentUser();
        if(user.getRoles().stream().anyMatch(role -> "ROLE_HOTEL".equals(role.getName()))) {
            return reservationRepository.findAllByHotelUser(user).stream().map(Reservation::toDto).toList();
        } else if (user.getRoles().stream().anyMatch(role -> "ROLE_USER".equals(role.getName()))){
            return reservationRepository.findAllByPetOwnerUser(user).stream().map(Reservation::toDto).toList();
        }
        return new ArrayList<>();
    }

    @Override
    public void acceptReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatusEnum.ACCEPTED.getCode());
        reservationRepository.save(reservation);
    }

    //    @Override
//    public List<ReservationResponseDto> getConflictedReservations(Long id) {
//        Reservation reservation = reservationRepository.findById(id).orElse(null);
//        List<Reservation> reservations = reservationRepository.findAllByHotelHotelId(reservation.getHotel().getHotelId());
//        reservations.remove(reservation);
//        Map<PetType, Long> petTypeQtyMap = reservation.getPets().stream()
//                .map(Pet::getPetType)
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//    }
}
