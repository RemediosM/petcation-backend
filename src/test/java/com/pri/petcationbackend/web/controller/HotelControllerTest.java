package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.dao.RoomRepository;
import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.Role;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.service.HotelService;
import com.pri.petcationbackend.service.ReservationService;
import com.pri.petcationbackend.service.UserService;
import com.pri.petcationbackend.web.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserService userService;
    @Mock
    private HotelService hotelService;
    @Mock
    private ReservationService reservationService;
    @InjectMocks
    private HotelController hotelController;

    @Test
    void getHotels_whenMaxDistanceIsNull() {
        // given
        HotelRequestDto hotelRequestDto = new HotelRequestDto();
        List<HotelDto> expectedHotelList = new ArrayList<>();
        expectedHotelList.add(new HotelDto());
        given(hotelService.getHotels(hotelRequestDto)).willReturn(expectedHotelList);

        // when
        List<HotelDto> result = hotelController.getHotels(hotelRequestDto);

        // then
        assertEquals(expectedHotelList, result);
        assertEquals(15, hotelRequestDto.getMaxDistance());
    }

    @Test
    void getHotels_whenMaxDistanceIsZero() {
        // given
        HotelRequestDto hotelRequestDto = new HotelRequestDto();
        hotelRequestDto.setMaxDistance(0);
        List<HotelDto> expectedHotelList = new ArrayList<>();
        expectedHotelList.add(new HotelDto());
        given(hotelService.getHotels(hotelRequestDto)).willReturn(expectedHotelList);

        // when
        List<HotelDto> result = hotelController.getHotels(hotelRequestDto);

        // then
        assertEquals(expectedHotelList, result);
        assertEquals(15, hotelRequestDto.getMaxDistance());
    }

    @Test
    void getHotels_whenMaxDistanceIsLowerThanZero() {
        // given
        HotelRequestDto hotelRequestDto = new HotelRequestDto();
        hotelRequestDto.setMaxDistance(-2);
        List<HotelDto> expectedHotelList = new ArrayList<>();
        expectedHotelList.add(new HotelDto());
        given(hotelService.getHotels(hotelRequestDto)).willReturn(expectedHotelList);

        // when
        List<HotelDto> result = hotelController.getHotels(hotelRequestDto);

        // then
        assertEquals(expectedHotelList, result);
        assertEquals(15, hotelRequestDto.getMaxDistance());
    }

    @Test
    void getHotels_whenMaxDistanceIsGreaterThanZero() {
        // given
        HotelRequestDto hotelRequestDto = new HotelRequestDto();
        hotelRequestDto.setMaxDistance(7);
        List<HotelDto> expectedHotelList = new ArrayList<>();
        expectedHotelList.add(new HotelDto());
        given(hotelService.getHotels(hotelRequestDto)).willReturn(expectedHotelList);

        // when
        List<HotelDto> result = hotelController.getHotels(hotelRequestDto);

        // then
        assertEquals(expectedHotelList, result);
        assertEquals(7, hotelRequestDto.getMaxDistance());
    }

    @Test
    void getAllHotels_shouldReturnListOfHotels() {
        // given
        List<HotelDto> expectedHotelList = new ArrayList<>();
        expectedHotelList.add(new HotelDto());
        given(hotelService.getAllHotels()).willReturn(expectedHotelList);

        // when
        List<HotelDto> result = hotelController.getAllHotels();

        // then
        assertEquals(expectedHotelList, result);
    }

    @Test
    void getHotelById_shouldReturnHotelDetails() {
        // given
        long id = 1;
        HotelDetailsDto expectedHotelDetailsDto = new HotelDetailsDto();
        given(hotelService.getHotelById(id)).willReturn(expectedHotelDetailsDto);

        // when
        HotelDetailsDto result = hotelController.getHotelById(id);

        // then
        assertEquals(expectedHotelDetailsDto, result);
    }

    @Test
    void addHotelRate_whenUserHasNoUserRole() {
        // given
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Only pet owner can add rate for hotel", HttpStatus.BAD_REQUEST);
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(1L, 1L, BigDecimal.ZERO, "");
        User user = new User();
        user.setRoles(new HashSet<>());
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_whenRateRequestIsNull() {
        // given
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Rate is empty!", HttpStatus.BAD_REQUEST);
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(null);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_whenRateIsNull() {
        // given
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Rate is empty!", HttpStatus.BAD_REQUEST);
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(1L, 1L, null, "");
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_whenRateIsZero() {
        // given
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Rate is empty!", HttpStatus.BAD_REQUEST);
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(1L, 1L, BigDecimal.ZERO, "");
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_whenHotelIdIsNull() {
        // given
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Hotel is empty!", HttpStatus.BAD_REQUEST);
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(null, 1L, BigDecimal.valueOf(2), "");
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_whenRateIsBiggerThanFive() {
        // given
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Only rates in range from 1 to 5", HttpStatus.BAD_REQUEST);
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(1L, 1L, BigDecimal.valueOf(6), "");
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_whenRateIsLowerThanOne() {
        // given
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Only rates in range from 1 to 5", HttpStatus.BAD_REQUEST);
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(1L, 1L, BigDecimal.valueOf(-1), "");
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_whenReservationIsNull() {
        // given
        long reservationId = 1L;
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("There are no reservations to rate", HttpStatus.BAD_REQUEST);
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(1L, reservationId, BigDecimal.valueOf(2), "");
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.empty());

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_whenReservationIsAnyRateForHotel() {
        // given
        long reservationId = 1L;
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("There are no reservations to rate", HttpStatus.BAD_REQUEST);
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(1L, reservationId, BigDecimal.valueOf(2), "");
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setIsAnyRateForHotel(true);
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_whenReservationIsNotCompleted() {
        // given
        long reservationId = 1L;
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("There are no reservations to rate", HttpStatus.BAD_REQUEST);
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(1L, reservationId, BigDecimal.valueOf(2), "");
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setIsAnyRateForHotel(false);
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));
        given(reservationService.isReservationCompleted(reservationId)).willReturn(false);

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }

    @Test
    void addHotelRate_shouldAddRate() {
        // given
        long reservationId = 1L;
        HotelRateRequestDto hotelRateRequestDto = new HotelRateRequestDto(1L, reservationId, BigDecimal.valueOf(2), "");
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        Role role = new Role(RoleEnum.ROLE_USER.name());
        User user = new User();
        user.setRoles(Set.of(role));
        Reservation reservation = new Reservation();
        reservation.setIsAnyRateForHotel(false);
        given(userService.getCurrentUser()).willReturn(user);
        given(reservationService.findById(reservationId)).willReturn(Optional.of(reservation));
        given(reservationService.isReservationCompleted(reservationId)).willReturn(true);
        willDoNothing().given(reservationService).save(reservation);
        willDoNothing().given(hotelService).addHotelRate(hotelRateRequestDto, user);

        // when
        ResponseEntity<String> result = hotelController.addHotelRate(hotelRateRequestDto);

        // then
        assertEquals(expectedResponse, result);
    }


}