package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.dao.ReservationRepository;
import com.pri.petcationbackend.dao.RoomRepository;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.service.HotelService;
import com.pri.petcationbackend.service.ReservationService;
import com.pri.petcationbackend.web.dto.HotelDto;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;
import com.pri.petcationbackend.web.dto.ReservationResponseDto;
import com.pri.petcationbackend.web.dto.RoomDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Tag(name = "Hotels")
@RequiredArgsConstructor
public class HotelController {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final HotelService hotelService;

    @GetMapping("/hotels")
    @Operation(summary = "Get hotels by coordinates and date.")
    public List<HotelDto> getHotels(@RequestParam(value = "latitude") Double lat, @RequestParam(value = "longitude") Double lon,
                                    @RequestParam(value = "dateFrom") Date dateFrom, @RequestParam(value = "dateTo") Date dateTo) {



        return  hotelService.getAllHotels();
    }

    @GetMapping("/room")
    @Operation(summary = "Get room by id.")
    public RoomDto getRoomById(@RequestParam(value = "id") Long id) {
        return roomRepository.findById(id).stream().map(Room::toDto).findAny().orElse(null);
    }

    @GetMapping("/reservation")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get reservation by id.")
    public ReservationResponseDto getReservationById(@RequestParam(value = "id") Long id) {
        return reservationRepository.findById(id).stream().map(Reservation::toDto).findAny().orElse(null);
    }

    @PostMapping("/addReservation")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add reservation.")
    public ResponseEntity<String> addReservation(@RequestBody @Valid ReservationRequestDto reservationDto) {
        reservationService.addReservation(reservationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteReservation")
    @Operation(summary = "Delete reservation by id.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteReservationById(@RequestParam(value = "id") Long id) {
        reservationRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
