package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.dao.RoomRepository;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.service.HotelService;
import com.pri.petcationbackend.service.ReservationService;
import com.pri.petcationbackend.service.UserService;
import com.pri.petcationbackend.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Tag(name = "Hotels")
@RequiredArgsConstructor
@CrossOrigin
public class HotelController {

    private final RoomRepository roomRepository;
    private final UserService userService;
    private final HotelService hotelService;
    private final ReservationService reservationService;


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

    @PostMapping("/addHotelRate")
    @Operation(summary = "Add rate for horel.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> addHotelRate(@RequestBody @Valid HotelRateRequestDto hotelRateRequestDto) {
        User user = userService.getCurrentUser();
        if(user.getRoles().stream().anyMatch(role -> "ROLE_HOTEL".equals(role.getName()))){
            return new ResponseEntity<>("Only pet owner can add rate for hotel", HttpStatus.BAD_REQUEST);
        }

        if(hotelRateRequestDto == null || hotelRateRequestDto.getRate() == null || BigDecimal.ZERO.equals(hotelRateRequestDto.getRate()))
            return new ResponseEntity<>("Rate is empty!", HttpStatus.BAD_REQUEST);

        if(hotelRateRequestDto.getHotelId() == null)
            return new ResponseEntity<>("Hotel is empty!", HttpStatus.BAD_REQUEST);

        if(hotelRateRequestDto.getRate().compareTo(BigDecimal.valueOf(5)) > 0 || hotelRateRequestDto.getRate().compareTo(BigDecimal.valueOf(1)) < 0)
            return new ResponseEntity<>("Only rates in range from 1 to 5", HttpStatus.BAD_REQUEST);
        Long reservationId = hotelRateRequestDto.getReservationId();
        Reservation reservation = null;
        if(reservationId != null) {
            reservation = reservationService.findById(reservationId).orElse(null);
        }
        if(reservation == null || !ReservationStatusEnum.ACCEPTED.getCode().equals(reservation.getStatus()) || BooleanUtils.isTrue(reservation.getIsAnyRateForHotel())) {
            return new ResponseEntity<>("There are no reservations to rate", HttpStatus.BAD_REQUEST);
        }
        reservation.setIsAnyRateForHotel(true);
        reservationService.save(reservation);

        hotelService.addHotelRate(hotelRateRequestDto, userService.getCurrentUser());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
