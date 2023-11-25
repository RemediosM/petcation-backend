package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.HotelRepository;
import com.pri.petcationbackend.model.Hotel;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.utils.DistanceUtils;
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

@Service
@Transactional
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

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

    private boolean isPeriodFree(LocalDate from, LocalDate to, List<Reservation> reservations) {
        return reservations.isEmpty()
                || reservations.stream()
                .anyMatch(reservation -> (reservation.getFrom().isBefore(from) || reservation.getFrom().isAfter(to))
                        && (reservation.getTo().isBefore(from) || reservation.getTo().isAfter(to)));
    }

    private boolean equalsNullOrZero(Double x) {
        return x == null || x == 0;
    }

}
