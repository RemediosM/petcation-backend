package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.HotelRepository;
import com.pri.petcationbackend.model.Hotel;
import com.pri.petcationbackend.web.dto.HotelDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    public List<HotelDto> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAllActiveHotels();
        List<HotelDto> hotelDtos = new ArrayList<>();
        hotels.forEach(hotel -> {
            HotelDto hotelDto = new HotelDto(hotel);
                }
        );
        return Collections.emptyList();

    }


}
