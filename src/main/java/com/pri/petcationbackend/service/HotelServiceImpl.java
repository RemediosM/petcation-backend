package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.HotelRepository;
import com.pri.petcationbackend.dao.ReservationRepository;
import com.pri.petcationbackend.dao.RoomRepository;
import com.pri.petcationbackend.model.Hotel;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.web.dto.HotelDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<HotelDto> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAllActiveHotels();
        List<HotelDto> hotelDtos = new ArrayList<>();
        hotels.forEach(hotel -> {
            HotelDto hotelDto = new HotelDto(hotel);
            hotelDto.setReservations(reservationRepository.findAllByHotel(hotel).stream().map(Reservation::toDto).toList());
            hotelDto.setRooms(roomRepository.findAllByHotel(hotel).stream().map(Room::toDto).toList());

        });
        return hotelDtos;

    }

}
