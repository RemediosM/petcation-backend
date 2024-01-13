package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.*;
import com.pri.petcationbackend.model.*;
import com.pri.petcationbackend.web.dto.HotelDetailsDto;
import com.pri.petcationbackend.web.dto.HotelDto;
import com.pri.petcationbackend.web.dto.HotelRateRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private PetTypeRepository petTypeRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private HotelRateRepository hotelRateRepository;
    @Mock
    private PetOwnerRepository petOwnerRepository;
    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void getAllHotels_shouldReturnHotels() {
        // given
        long hotel1Id = 1L;
        long hotel2Id = 2L;
        long hotel3Id = 3L;
        double hotel1AverageRate = 3.115;
        Double hotel2AverageRate = 3.114;
        Double hotel3AverageRate = null;
        Hotel hotel1 = new Hotel();
        hotel1.setHotelId(hotel1Id);
        Hotel hotel2 = new Hotel();
        hotel2.setHotelId(hotel2Id);
        Hotel hotel3 = new Hotel();
        hotel3.setHotelId(hotel3Id);
        HotelDto expectedHotelDto1 = new HotelDto();
        expectedHotelDto1.setId(hotel1Id);
        expectedHotelDto1.setAverageRate(BigDecimal.valueOf(3.12));
        expectedHotelDto1.setImages(List.of());
        expectedHotelDto1.setRooms(List.of());
        HotelDto expectedHotelDto2 = new HotelDto();
        expectedHotelDto2.setId(hotel2Id);
        expectedHotelDto2.setAverageRate(BigDecimal.valueOf(3.11));
        expectedHotelDto2.setImages(List.of());
        expectedHotelDto2.setRooms(List.of());
        HotelDto expectedHotelDto3 = new HotelDto();
        expectedHotelDto3.setId(hotel3Id);
        expectedHotelDto3.setImages(List.of());
        expectedHotelDto3.setRooms(List.of());
        given(hotelRepository.findAllActiveHotels()).willReturn(List.of(hotel1, hotel2, hotel3));
        given(hotelRateRepository.getAverageRateForHotel(hotel1Id)).willReturn(hotel1AverageRate);
        given(hotelRateRepository.getAverageRateForHotel(hotel2Id)).willReturn(hotel2AverageRate);
        given(hotelRateRepository.getAverageRateForHotel(hotel3Id)).willReturn(hotel3AverageRate);

        // when
        List<HotelDto> result = hotelService.getAllHotels();

        // then
        assertTrue(result.stream().anyMatch(hotelDto -> hotelDto.equals(expectedHotelDto1)));
        assertTrue(result.stream().anyMatch(hotelDto -> hotelDto.equals(expectedHotelDto2)));
        assertTrue(result.stream().anyMatch(hotelDto -> hotelDto.equals(expectedHotelDto3)));
    }

    @Test
    void getHotelById_whenNoHotelFound() {
        // given
        long hotelId = 1L;
        given(hotelRepository.findById(hotelId)).willReturn(Optional.empty());

        // when
        HotelDetailsDto result = hotelService.getHotelById(hotelId);

        // then
        assertNull(result);
    }

    @Test
    void getHotelById_shouldReturnHotel() {
        // given
        long hotelId = 1L;
        String hotelName = "name";
        LocalDate dateTo = null;
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);
        hotel.setName("name");
        hotel.setAddress(new Address());
        PetOwner petOwner1 = new PetOwner();
        HotelRate rate1 = new HotelRate();
        rate1.setRate(1.0);
        rate1.setPetOwner(petOwner1);
        PetOwner petOwner2 = new PetOwner();
        HotelRate rate2 = new HotelRate();
        rate2.setRate(2.0);
        rate2.setPetOwner(petOwner2);
        given(hotelRepository.findById(hotelId)).willReturn(Optional.of(hotel));
        given(reservationRepository.findLastReservationForHotel(hotelId)).willReturn(dateTo);
        given(hotelRateRepository.findAllByHotel(hotel)).willReturn(List.of(rate1, rate2));

        // when
        HotelDetailsDto result = hotelService.getHotelById(hotelId);

        // then
        assertEquals(hotelId, result.getId());
        assertEquals(hotelName, result.getName());
        assertNull(result.getDescription());
        assertNull(result.getAddressDto());
        assertFalse(result.getIsAnyReservation());
        assertEquals(BigDecimal.valueOf(1.50).setScale(2).setScale(2, RoundingMode.HALF_UP), result.getAverageRate());
        assertEquals(2, result.getRates().size());
        assertEquals(0, result.getAllAvailableRoomsByPetType().size());
        assertEquals(0, result.getImages().size());
    }

    @Test
    void addHotelRate_whenCurrentUserIsNull() {
        // given
        User user = null;

        // when
        hotelService.addHotelRate(new HotelRateRequestDto(1L, 1L, BigDecimal.ZERO, ""), user);

        // then
        verifyNoInteractions(petOwnerRepository);
    }

}