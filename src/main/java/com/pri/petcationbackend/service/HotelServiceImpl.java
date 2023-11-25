package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.HotelRepository;
import com.pri.petcationbackend.dao.PetTypeRepository;
import com.pri.petcationbackend.dao.ReservationRepository;
import com.pri.petcationbackend.model.Hotel;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.utils.DistanceUtils;
import com.pri.petcationbackend.web.controller.HotelDetailsDto;
import com.pri.petcationbackend.web.dto.HotelDto;
import com.pri.petcationbackend.web.dto.HotelRequestDto;
import com.pri.petcationbackend.web.dto.PetTypeQtyDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final PetTypeRepository petTypeRepository;

    @Override
    public List<HotelDto> getAllHotels() {
        return hotelRepository.findAllActiveHotels().stream()
                .map(Hotel::toDto)
                .toList();
    }

    public List<HotelDto> getHotels(HotelRequestDto hotelRequestDto) {
        return hotelRepository.findAllActiveHotels().stream()
                .filter(hotel -> equalsNullOrZero(hotelRequestDto.getLat()) || equalsNullOrZero(hotelRequestDto.getLon()) || DistanceUtils.isTwoPointsInMaxDistance(hotelRequestDto.getMaxDistance(), hotelRequestDto.getLat(), hotelRequestDto.getLon(), hotel.getAddress().getLatitude(), hotel.getAddress().getLongitude()))
                .filter(hotel -> isRoomVacancyWithPetType(hotel.getRooms(), hotelRequestDto.getFrom(), hotelRequestDto.getTo(), hotelRequestDto.getPetTypeQtyDtoList()))
                .map(Hotel::toDto)
                .toList();
    }

    @Override
    public HotelDetailsDto getHotelById(Long id) {

        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotel != null) {
            LocalDate from = LocalDate.now();
            LocalDate to = reservationRepository.findLastReservationForHotel(id);
            boolean isAnyReservation = to != null;
            return new HotelDetailsDto(hotel.getHotelId(), hotel.getName(), hotel.getDescription(), hotel.getAddress().toDto(),
                    isAnyReservation, getPetTypeQtyList(hotel.getRooms()), isAnyReservation ? getFreeRoomsList(hotel.getRooms(), from, to) : null);
        }
        return null;
    }

    private boolean isRoomVacancyWithPetType(List<Room> rooms, LocalDate from, LocalDate to, List<PetTypeQtyDto> petTypeQtyDtoList) {

        if(CollectionUtils.isEmpty(petTypeQtyDtoList)) {
            return rooms.stream()
                    .anyMatch(room -> isPeriodFree(from, to, room.getReservations()));
        }
        List<Boolean> roomsAvailability = new ArrayList<>();
        petTypeQtyDtoList.forEach(petTypeQtyDto -> {
            List<Room> filteredRooms = rooms.stream()
                    .filter(room -> (petTypeQtyDto.getPetType() == null || room.getPetTypes().stream().anyMatch(type -> petTypeQtyDto.getPetType().equals(type.getName())))
                            && isPeriodFree(from, to, room.getReservation()))
                    .toList();
            if (filteredRooms.size() >= petTypeQtyDto.getQty()) {
                rooms.removeAll(filteredRooms);
                roomsAvailability.add(true);
            } else {
                roomsAvailability.add(false);
            }
        });
        return !roomsAvailability.contains(false);

    }

    private Map<LocalDate, List<PetTypeQtyDto>> getFreeRoomsList(List<Room> rooms, LocalDate from, LocalDate to) {
        Map<LocalDate, List<PetTypeQtyDto>> dateListMap = new TreeMap<>();

        from.datesUntil(to.plusDays(1)).forEach(localDate -> {
            List<PetTypeQtyDto> petTypeQtyDtoList = new ArrayList<>();
            petTypeRepository.findAll().forEach(petType -> {
                List<Room> filteredRooms = rooms.stream()
                        .filter(room -> room.getPetTypes().stream().anyMatch(type -> petType.getName().equals(type.getName()))
                                && isDayFree(localDate, room.getReservation()))
                        .toList();
                if(!filteredRooms.isEmpty()) {
                    petTypeQtyDtoList.add(new PetTypeQtyDto(petType.getName(), (long) filteredRooms.size(), filteredRooms.get(0).getPrice()));
                }
            });
            if(!petTypeQtyDtoList.isEmpty()) {
                dateListMap.put(localDate, petTypeQtyDtoList);
            }
        });

        return dateListMap;
    }

    private List<PetTypeQtyDto> getPetTypeQtyList(List<Room> rooms) {

            List<PetTypeQtyDto> petTypeQtyDtoList = new ArrayList<>();
            petTypeRepository.findAll().forEach(petType -> {
                List<Room> filteredRooms = rooms.stream()
                        .filter(room -> room.getPetTypes().stream().anyMatch(type -> petType.getName().equals(type.getName())))
                        .toList();
                if(!filteredRooms.isEmpty()) {
                    petTypeQtyDtoList.add(new PetTypeQtyDto(petType.getName(), (long) filteredRooms.size(), filteredRooms.get(0).getPrice()));
                }
            });

        return petTypeQtyDtoList;
    }

    private boolean isPeriodFree(LocalDate from, LocalDate to, List<Reservation> reservations) {
        return reservations.isEmpty()
                || reservations.stream()
                .anyMatch(reservation -> (reservation.getFrom().isBefore(from) || reservation.getFrom().isAfter(to))
                        && (reservation.getTo().isBefore(from) || reservation.getTo().isAfter(to)));
    }

    private boolean isDayFree(LocalDate date, List<Reservation> reservations) {
        return reservations.isEmpty()
                || reservations.stream()
                .anyMatch(reservation -> !((date.isAfter(reservation.getFrom()) || date.isEqual(reservation.getFrom()))
                        && date.isBefore(reservation.getTo())) || date.isEqual(reservation.getTo()));
    }

    private boolean equalsNullOrZero(Double x) {
        return x == null || x == 0;
    }

}
