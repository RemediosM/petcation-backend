package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.dao.ReservationRepository;
import com.pri.petcationbackend.dao.RoomRepository;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.service.HotelService;
import com.pri.petcationbackend.service.ReservationService;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;
import com.pri.petcationbackend.web.dto.ReservationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Reservations")
@RequiredArgsConstructor
@CrossOrigin
public class ReservationController {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final HotelService hotelService;

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
        List<Room> availableRooms = hotelService.checkAvailableRooms(reservationDto);
        if(CollectionUtils.isEmpty(availableRooms)) {
            return new ResponseEntity<>("There are no available rooms!", HttpStatus.BAD_REQUEST);
        }
        reservationService.addReservation(availableRooms, reservationDto);
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
