package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.PetRepository;
import com.pri.petcationbackend.dao.ReservationRepository;
import com.pri.petcationbackend.model.*;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;
import com.pri.petcationbackend.web.dto.ReservationResponseDto;
import com.pri.petcationbackend.web.dto.ReservationStatusEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final PetRepository petRepository;

    private final HotelService hotelService;

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
    public void acceptReservation(Reservation reservation, long availableRoomsSize) {
        reservation.setStatus(ReservationStatusEnum.ACCEPTED.getCode());
        List<Reservation> reservationsToSave = new ArrayList<>();
        findConflictedReservations(reservation, availableRoomsSize).forEach(res -> {
            res.setStatus(ReservationStatusEnum.REJECTED.getCode());
            reservationsToSave.add(res);
        });
        reservationsToSave.add(reservation);
        reservationRepository.saveAll(reservationsToSave);
    }

    @Override
    public List<ReservationResponseDto> getConflictedReservations(Reservation reservation, long availableRoomsSize) {
        if (reservation == null) {
            return Collections.emptyList();
        }
        Set<Reservation> conflictedReservations = findConflictedReservations(reservation, availableRoomsSize);
        return conflictedReservations.stream().map(Reservation::toDto).sorted(Comparator.comparing(ReservationResponseDto::getFrom)).toList();
    }

    private Set<Reservation> findConflictedReservations(Reservation reservation, long availableRoomsSize) {
        Long hotelId = reservation.getHotel().getHotelId();
        List<Reservation> reservations = reservationRepository.findAllByHotelHotelId(hotelId);
        reservations.remove(reservation);
        Map<PetType, Long> petTypeQtyMap = reservation.getPets().stream()
                .map(Pet::getPetType)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Set<Reservation> conflictedReservations = new HashSet<>();
        if(reservation.getFrom() != null && reservation.getTo() != null) {
            int cnt = 0;
            for (Reservation r: reservations) {

                List<Room> roomList = r.getRooms();
                List<Room> takenRooms = new ArrayList<>();
                List<Room> rooms = new ArrayList<>();
                for (Map.Entry<PetType, Long> entry : petTypeQtyMap.entrySet()) {

                    List<Room> filteredRooms = roomList.stream()
                            .filter(room -> (entry.getKey() == null || room.getPetType().getName().equals(entry.getKey().getName()))
                                    && !isPeriodFree(reservation.getFrom(), reservation.getFrom(), r))
                            .collect(Collectors.toList());
                    long petTypeRoomsCount = r.getHotel().getRooms().stream().filter(room -> room.getPetType().equals(entry.getKey())).count();
                    long diff = petTypeRoomsCount - filteredRooms.size() - cnt;
                    cnt += filteredRooms.size();
                    rooms.addAll(filteredRooms);
                    if (diff <= entry.getValue()) {
                        takenRooms.addAll(rooms);
                    }
                    filteredRooms.clear();
                }
                long diff = availableRoomsSize - reservation.getRooms().size();
                if(diff < takenRooms.size()) {
                    takenRooms.forEach(takenRoom ->
                            conflictedReservations.addAll(takenRoom.getReservations().stream()
                                    .filter(res -> !res.equals(reservation))
                                    .filter(res -> !res.getStatus().equals(ReservationStatusEnum.ACCEPTED.getCode()))
                                    .collect(Collectors.toSet())));
                }
            }
        }
        return conflictedReservations;
    }

    private static boolean isPeriodFree(LocalDate from, LocalDate to, Reservation reservation) {
        return (!reservation.isAccepted() && !reservation.isPending())
                || (reservation.getFrom().isBefore(from) || reservation.getFrom().isAfter(to))
                        && (reservation.getTo().isBefore(from) || reservation.getTo().isAfter(to));
    }

    @Override
    public void save(Reservation reservation) {
        if(reservation != null) {
            reservationRepository.save(reservation);
        }
    }
}
