package com.pri.petcationbackend.service;

import com.pri.petcationbackend.web.controller.HotelDetailsDto;
import com.pri.petcationbackend.web.dto.HotelDto;
import com.pri.petcationbackend.web.dto.HotelRequestDto;

import java.util.List;

public interface HotelService {
    List<HotelDto> getAllHotels();
    List<HotelDto> getHotels(HotelRequestDto hotelRequestDto);

    HotelDetailsDto getHotelById(Long id);
}
