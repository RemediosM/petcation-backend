package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.service.HotelService;
import com.pri.petcationbackend.service.ReservationService;
import com.pri.petcationbackend.service.UserService;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;
import com.pri.petcationbackend.web.dto.ReservationResponseDto;
import com.pri.petcationbackend.web.dto.ReservationStatusEnum;
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

    private final ReservationService reservationService;
    private final HotelService hotelService;

    private final UserService userService;

    @GetMapping("/reservation")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get reservation by id.")
    public ReservationResponseDto getReservationById(@RequestParam(value = "id") Long id) {
        return reservationService.getReservationById(id);
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
        User user = userService.getCurrentUser();
        if(user.getRoles().stream().anyMatch(role -> "ROLE_HOTEL".equals(role.getName()))){
            return new ResponseEntity<>("Only pet owner can delete a reservation. Use the reject reservation function.", HttpStatus.BAD_REQUEST);
        }
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/rejectReservation")
    @Operation(summary = "Reject reservation by id.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> rejectReservationById(@RequestParam(value = "id") Long id) {
        User user = userService.getCurrentUser();
        if(user.getRoles().stream().anyMatch(role -> "ROLE_USER".equals(role.getName()))){
            return new ResponseEntity<>("Only hotel can reject a reservation. Use the delete reservation function.", HttpStatus.BAD_REQUEST);
        }
        Reservation reservation = reservationService.findById(id).orElse(null);
        if(reservation == null) {
            return new ResponseEntity<>("There is no reservation with the given id", HttpStatus.BAD_REQUEST);
        }
        if(!ReservationStatusEnum.PENDING.getCode().equals(reservation.getStatus()) && !ReservationStatusEnum.ACCEPTED.getCode().equals(reservation.getStatus())){
            return new ResponseEntity<>("The reservation does not have the status Pending or Accepted", HttpStatus.BAD_REQUEST);
        }
        reservationService.rejectReservation(reservation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/currentReservations")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get current reservations for user.")
    public List<ReservationResponseDto> getCurrentReservations() {
        return reservationService.getCurrentReservationsForUser();
    }

    @GetMapping("/allReservations")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get all reservations for user.")
    public List<ReservationResponseDto> getAllReservations() {
        return reservationService.getAllReservationsForUser();
    }

    @GetMapping("/getConflictedReservations")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get conflicted reservations by reservation id.")
    public ResponseEntity<List<ReservationResponseDto>> getConflictedReservations(@RequestParam(value = "id") Long id) {
        User user = userService.getCurrentUser();
        if(user.getRoles().stream().anyMatch(role -> "ROLE_USER".equals(role.getName()))){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Reservation reservation = reservationService.findById(id).orElse(null);
        if(reservation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!ReservationStatusEnum.PENDING.getCode().equals(reservation.getStatus())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Room> availableRooms = hotelService.checkAvailableRooms(new ReservationRequestDto(reservation));
        if(CollectionUtils.isEmpty(availableRooms)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(reservationService.getConflictedReservations(reservation, availableRooms.size()), HttpStatus.OK);
    }

    @PostMapping("/acceptReservation")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Accept reservation.")
    public ResponseEntity<String> acceptReservation(@RequestParam(value = "id") Long id) {
        User user = userService.getCurrentUser();
        if(user.getRoles().stream().anyMatch(role -> "ROLE_USER".equals(role.getName()))){
            return new ResponseEntity<>("Only hotel can accept a reservation.", HttpStatus.BAD_REQUEST);
        }
        Reservation reservation = reservationService.findById(id).orElse(null);
        if(reservation == null) {
            return new ResponseEntity<>("There is no reservation with the given id", HttpStatus.BAD_REQUEST);
        }
        if(!ReservationStatusEnum.PENDING.getCode().equals(reservation.getStatus())){
            return new ResponseEntity<>("The reservation does not have the status Pending", HttpStatus.BAD_REQUEST);
        }
        List<Room> availableRooms = hotelService.checkAvailableRooms(new ReservationRequestDto(reservation));
        if(CollectionUtils.isEmpty(availableRooms)) {
            return new ResponseEntity<>("There are no available rooms!", HttpStatus.BAD_REQUEST);
        }
        reservationService.acceptReservation(reservation, availableRooms.size());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
