package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Role;
import com.pri.petcationbackend.model.Room;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.service.HotelService;
import com.pri.petcationbackend.service.ReservationService;
import com.pri.petcationbackend.service.UserService;
import com.pri.petcationbackend.web.dto.ReservationRequestDto;
import com.pri.petcationbackend.web.dto.ReservationResponseDto;
import com.pri.petcationbackend.web.dto.ReservationStatusEnum;
import com.pri.petcationbackend.web.dto.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;
    @Mock
    private HotelService hotelService;
    @Mock
    private UserService userService;
    @InjectMocks
    private ReservationController reservationController;

    @Test
    void getReservationById_shouldReturnReservation() {
        // given
        long reservationId = 1L;
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
        given(reservationService.getReservationById(reservationId)).willReturn(reservationResponseDto);

        // when
        ReservationResponseDto result = reservationController.getReservationById(reservationId);

        // then
        assertEquals(reservationResponseDto, result);
    }

    @Test
    void addReservation_whenNoAvailableRoom() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("There are no available rooms!", HttpStatus.BAD_REQUEST);
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
        given(hotelService.checkAvailableRooms(reservationRequestDto)).willReturn(List.of());

        // when
        ResponseEntity<String> result = reservationController.addReservation(reservationRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addReservation_shouldAddReservation() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
        Room room = new Room();
        List<Room> rooms = List.of(room);
        given(hotelService.checkAvailableRooms(reservationRequestDto)).willReturn(rooms);
        doNothing().when(reservationService).addReservation(rooms, reservationRequestDto);

        // when
        ResponseEntity<String> result = reservationController.addReservation(reservationRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void deleteReservationById_whenNoUserRole() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Only pet owner can delete a reservation. Use the reject reservation function.", HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = reservationController.deleteReservationById(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void deleteReservationById_shouldDeleteReservation() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);
        doNothing().when(reservationService).deleteReservation(reservationId);

        // when
        ResponseEntity<String> result = reservationController.deleteReservationById(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void rejectReservationById_whenNoHotelRole() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Only hotel can reject a reservation. Use the delete reservation function.", HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = reservationController.rejectReservationById(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void rejectReservationById_whenNoReservation() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>("There is no reservation with the given id", HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.empty());

        // when
        ResponseEntity<String> result = reservationController.rejectReservationById(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void rejectReservationById_whenReservationIsNotPendingAndNotAccepted() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>("The reservation does not have the status Pending or Accepted", HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));

        // when
        ResponseEntity<String> result = reservationController.rejectReservationById(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void rejectReservationById_shouldRejectReservationWhenIsPending() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatusEnum.PENDING.getCode());
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));
        doNothing().when(reservationService).rejectReservation(reservation);

        // when
        ResponseEntity<String> result = reservationController.rejectReservationById(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void rejectReservationById_shouldRejectReservationWhenIsAccepted() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatusEnum.ACCEPTED.getCode());
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));
        doNothing().when(reservationService).rejectReservation(reservation);

        // when
        ResponseEntity<String> result = reservationController.rejectReservationById(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void getCurrentReservations_shouldReturnReservations() {
        // given
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
        List<ReservationResponseDto> reservationResponseDtoList = List.of(reservationResponseDto);
        given(reservationService.getCurrentReservationsForUser()).willReturn(reservationResponseDtoList);

        // when
        List<ReservationResponseDto> result = reservationController.getCurrentReservations();

        // then
        assertEquals(reservationResponseDtoList, result);
    }

    @Test
    void getAllReservations_shouldReturnReservations() {
        // given
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
        List<ReservationResponseDto> reservationResponseDtoList = List.of(reservationResponseDto);
        given(reservationService.getAllReservationsForUser()).willReturn(reservationResponseDtoList);

        // when
        List<ReservationResponseDto> result = reservationController.getAllReservations();

        // then
        assertEquals(reservationResponseDtoList, result);
    }

    @Test
    void getConflictedReservations_whenNoHotelRole() {
        // when
        ResponseEntity<List<ReservationResponseDto>> responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        long reservationId = 1L;
        Role role = new Role();
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<List<ReservationResponseDto>> result = reservationController.getConflictedReservations(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void getConflictedReservations_whenNoReservation() {
        // when
        ResponseEntity<List<ReservationResponseDto>> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.empty());

        // when
        ResponseEntity<List<ReservationResponseDto>> result = reservationController.getConflictedReservations(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void getConflictedReservations_whenStatusIsNotPending() {
        // when
        ResponseEntity<List<ReservationResponseDto>> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatusEnum.PENDING.getCode());
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));

        // when
        ResponseEntity<List<ReservationResponseDto>> result = reservationController.getConflictedReservations(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void getConflictedReservations_whenNoAvailableRoom() {
        // when
        ResponseEntity<List<ReservationResponseDto>> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatusEnum.PENDING.getCode());
        List<Room> rooms = List.of();
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));
        given(hotelService.checkAvailableRooms(any(ReservationRequestDto.class))).willReturn(rooms);

        // when
        ResponseEntity<List<ReservationResponseDto>> result = reservationController.getConflictedReservations(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void getConflictedReservations_shouldReturnConflictedReservations() {
        // when
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
        List<ReservationResponseDto> reservationResponseDtoList = List.of(reservationResponseDto);
        ResponseEntity<List<ReservationResponseDto>> responseEntity = new ResponseEntity<>(reservationResponseDtoList,HttpStatus.OK);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatusEnum.PENDING.getCode());
        Room room = new Room();
        List<Room> rooms = List.of(room);
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));
        given(hotelService.checkAvailableRooms(any(ReservationRequestDto.class))).willReturn(rooms);
        given(reservationService.getConflictedReservations(reservation, rooms.size())).willReturn(reservationResponseDtoList);

        // when
        ResponseEntity<List<ReservationResponseDto>> result = reservationController.getConflictedReservations(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void acceptReservation_whenNoHotelRole() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Only hotel can accept a reservation.", HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = reservationController.acceptReservation(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void acceptReservation_whenNoReservation() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>("There is no reservation with the given id", HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.empty());

        // when
        ResponseEntity<String> result = reservationController.acceptReservation(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void acceptReservation_whenNoPendingStatus() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>("The reservation does not have the status Pending", HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatusEnum.ACCEPTED.getCode());
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));

        // when
        ResponseEntity<String> result = reservationController.acceptReservation(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void acceptReservation_whenNoAvailableRoom() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>("There are no available rooms!", HttpStatus.BAD_REQUEST);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatusEnum.PENDING.getCode());
        List<Room> rooms = List.of();
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));
        given(hotelService.checkAvailableRooms(any(ReservationRequestDto.class))).willReturn(rooms);

        // when
        ResponseEntity<String> result = reservationController.acceptReservation(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void acceptReservation_shouldAcceptReservation() {
        // when
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        long reservationId = 1L;
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatusEnum.PENDING.getCode());
        Room room = new Room();
        List<Room> rooms = List.of(room);
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));
        given(hotelService.checkAvailableRooms(any(ReservationRequestDto.class))).willReturn(rooms);
        doNothing().when(reservationService).acceptReservation(reservation, rooms.size());

        // when
        ResponseEntity<String> result = reservationController.acceptReservation(reservationId);

        // then
        assertEquals(responseEntity, result);
    }

}