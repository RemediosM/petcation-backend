package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.dao.ReservationRepository;
import com.pri.petcationbackend.dao.RoomRepository;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.service.HotelService;
import com.pri.petcationbackend.service.ReservationService;
import com.pri.petcationbackend.web.dto.HotelDetailsDto;
import com.pri.petcationbackend.web.dto.HotelDto;
import com.pri.petcationbackend.web.dto.HotelRequestDto;
import com.pri.petcationbackend.web.dto.RoomDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Hotels")
@RequiredArgsConstructor
@CrossOrigin
public class HotelController {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final HotelService hotelService;


    @PostMapping(value = "/hotels")
    @Operation(summary = "Get hotels by coordinates and date.")
    public List<HotelDto> getHotels(@RequestBody HotelRequestDto hotelRequestDto) {
        if (hotelRequestDto.getMaxDistance() == null || hotelRequestDto.getMaxDistance() == 0) {
            hotelRequestDto.setMaxDistance(15);
        }
        return hotelService.getHotels(hotelRequestDto);
    }

    @GetMapping("/allHotels")
    @Operation(summary = "Get all hotels.")
    @CrossOrigin
    public List<HotelDto> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotel")
    @Operation(summary = "Get hotel by id.")
    public HotelDetailsDto getHotelById(@RequestParam Long id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping("/room")
    @Operation(summary = "Get room by id.")
    public RoomDto getRoomById(@RequestParam(value = "id") Long id) {
        return roomRepository.findById(id).stream().map(Room::toDto).findAny().orElse(null);
    }

}
