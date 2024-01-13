package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.PetOwner;
import com.pri.petcationbackend.model.Role;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.service.PetService;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private PetService petService;
    @Mock
    private UserService userService;
    @Mock
    private ReservationService reservationService;
    @InjectMocks
    private PetController petController;


    @Test
    void getPetsForUser_shouldReturnListOfPets() {
        // given
        PetResponseDto petResponseDto = new PetResponseDto();
        List<PetResponseDto> petResponseDtoList = List.of(petResponseDto);
        User user = new User();
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.getAllPetsByUser(user)).willReturn(petResponseDtoList);

        // when
        List<PetResponseDto> result = petController.getPetsForUser();

        // then
        assertEquals(petResponseDtoList, result);
    }

    @Test
    void getPets_shouldReturnListOfPets() {
        // given
        PetResponseDto petResponseDto = new PetResponseDto();
        List<PetResponseDto> petResponseDtoList = List.of(petResponseDto);
        given(petService.getAllPets()).willReturn(petResponseDtoList);

        // when
        List<PetResponseDto> result = petController.getPets();

        // then
        assertEquals(petResponseDtoList, result);
    }

    @Test
    void addPet_whenNoName() {
        // given
        PetDto petDto = new PetDto();
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Name is empty!", HttpStatus.BAD_REQUEST);

        // when
        ResponseEntity<String> result = petController.addPet(petDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPet_whenNoPetType() {
        // given
        PetDto petDto = new PetDto();
        petDto.setName("name");
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Type is empty!", HttpStatus.BAD_REQUEST);

        // when
        ResponseEntity<String> result = petController.addPet(petDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPet_shouldModifyPet() {
        // given
        PetDto petDto = new PetDto();
        petDto.setName("name");
        petDto.setPetType(PetTypeEnum.CAT);
        petDto.setId(1L);
        User user = new User();
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Pet is modified successfully!", HttpStatus.OK);
        given(userService.getCurrentUser()).willReturn(user);
        doNothing().when(petService).addModifyPet(petDto, user);

        // when
        ResponseEntity<String> result = petController.addPet(petDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPet_shouldAddPet() {
        // given
        PetDto petDto = new PetDto();
        petDto.setName("name");
        petDto.setPetType(PetTypeEnum.CAT);
        User user = new User();
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Pet is added successfully!", HttpStatus.OK);
        given(userService.getCurrentUser()).willReturn(user);
        doNothing().when(petService).addModifyPet(petDto, user);

        // when
        ResponseEntity<String> result = petController.addPet(petDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetsImages_whenNoPetId() {
        // given
        PetImagesDto petImagesDto = new PetImagesDto();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // when
        ResponseEntity<String> result = petController.addPetsImages(petImagesDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetsImages_whenNoImageUrls() {
        // given
        PetImagesDto petImagesDto = new PetImagesDto();
        petImagesDto.setPetId(1L);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // when
        ResponseEntity<String> result = petController.addPetsImages(petImagesDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetsImages_whenNoUserRole() {
        // given
        long petId = 1L;
        PetImagesDto petImagesDto = new PetImagesDto();
        petImagesDto.setPetId(petId);
        petImagesDto.setImageUrls(List.of("url"));
        User user = new User();
        user.setRoles(Set.of(new Role()));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.findPetById(petId)).willReturn(null);

        // when
        ResponseEntity<String> result = petController.addPetsImages(petImagesDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetsImages_whenNoPet() {
        // given
        long petId = 1L;
        PetImagesDto petImagesDto = new PetImagesDto();
        petImagesDto.setPetId(1L);
        petImagesDto.setImageUrls(List.of("url"));
        User user = new User();
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        user.setRoles(Set.of(role));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.findPetById(petId)).willReturn(null);

        // when
        ResponseEntity<String> result = petController.addPetsImages(petImagesDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetsImages_whenNoPetOwner() {
        // given
        long petId = 1L;
        PetImagesDto petImagesDto = new PetImagesDto();
        petImagesDto.setPetId(1L);
        petImagesDto.setImageUrls(List.of("url"));
        User user = new User();
        Role role = new Role();
        role.setName(RoleEnum.ROLE_USER.name());
        user.setRoles(Set.of(role));
        Pet pet = new Pet();
        pet.setPetOwner(new PetOwner());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.findPetById(petId)).willReturn(null);

        // when
        ResponseEntity<String> result = petController.addPetsImages(petImagesDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetsImages_whenUserIsNotPetOwner() {
        // given
        long petId = 1L;
        PetImagesDto petImagesDto = new PetImagesDto();
        petImagesDto.setPetId(1L);
        petImagesDto.setImageUrls(List.of("url"));
        User user = new User();
        user.setUserId(1L);
        User petOwnerUser = new User();
        petOwnerUser.setUserId(2L);
        PetOwner petOwner = new PetOwner(petOwnerUser);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        user.setRoles(Set.of(role));
        Pet pet = new Pet();
        pet.setPetOwner(petOwner);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.findPetById(petId)).willReturn(pet);

        // when
        ResponseEntity<String> result = petController.addPetsImages(petImagesDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetsImages_shouldAddImages() {
        // given
        long petId = 1L;
        PetImagesDto petImagesDto = new PetImagesDto();
        petImagesDto.setPetId(1L);
        petImagesDto.setImageUrls(List.of("url"));
        User user = new User();
        user.setUserId(1L);
        PetOwner petOwner = new PetOwner(user);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_USER.name());
        user.setRoles(Set.of(role));
        Pet pet = new Pet();
        pet.setPetOwner(petOwner);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.findPetById(petId)).willReturn(pet);
        doNothing().when(petService).addImagesForPet(pet, petImagesDto.getImageUrls());

        // when
        ResponseEntity<String> result = petController.addPetsImages(petImagesDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void deleteImage_shouldDeleteImage() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        long imageId = 1L;
        doNothing().when(petService).deleteImage(imageId);

        // when
        ResponseEntity<String> result = petController.deleteImage(imageId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void deleteById_shouldDeletePet() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        long petId = 1L;
        doNothing().when(petService).delete(petId);

        // when
        ResponseEntity<String> result = petController.deleteById(petId);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenNoHotelRole() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Only hotel can add rate for pet", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = petController.addPetRate(null);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenRequestIsNull() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Rate is empty!", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = petController.addPetRate(null);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenRateIsNull() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Rate is empty!", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        long petId = 1L;
        long reservationId = 1L;
        BigDecimal rate = null;
        String comment = "";
        PetRateRequestDto petRateRequestDto = new PetRateRequestDto(petId, reservationId, rate, comment);
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = petController.addPetRate(petRateRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenRateIsZero() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Rate is empty!", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        long petId = 1L;
        long reservationId = 1L;
        BigDecimal rate = BigDecimal.ZERO;
        String comment = "";
        PetRateRequestDto petRateRequestDto = new PetRateRequestDto(petId, reservationId, rate, comment);
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = petController.addPetRate(petRateRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenPetIdIsNull() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Pet is empty!", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        long reservationId = 1L;
        BigDecimal rate = BigDecimal.ONE;
        String comment = "";
        PetRateRequestDto petRateRequestDto = new PetRateRequestDto(null, reservationId, rate, comment);
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = petController.addPetRate(petRateRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenReservationIdIsNull() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Reservation is empty!", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        long petId = 1L;
        BigDecimal rate = BigDecimal.ONE;
        String comment = "";
        PetRateRequestDto petRateRequestDto = new PetRateRequestDto(petId, null, rate, comment);
        given(userService.getCurrentUser()).willReturn(user);

        // when
        ResponseEntity<String> result = petController.addPetRate(petRateRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenRateForPetAndReservationExists() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("There are no reservations to rate", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        long petId = 1L;
        long reservationId = 1L;
        BigDecimal rate = BigDecimal.ONE;
        String comment = "";
        PetRateRequestDto petRateRequestDto = new PetRateRequestDto(petId, reservationId, rate, comment);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.checkIfRateForPetAndReservationExists(petId, reservationId)).willReturn(true);

        // when
        ResponseEntity<String> result = petController.addPetRate(petRateRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenReservationIsNotCopleted() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("There are no reservations to rate", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        long petId = 1L;
        long reservationId = 1L;
        BigDecimal rate = BigDecimal.ONE;
        String comment = "";
        PetRateRequestDto petRateRequestDto = new PetRateRequestDto(petId, reservationId, rate, comment);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.checkIfRateForPetAndReservationExists(petId, reservationId)).willReturn(false);
        given(reservationService.isReservationCompleted(reservationId)).willReturn(false);

        // when
        ResponseEntity<String> result = petController.addPetRate(petRateRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenRateIsBiggerThanFive() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Only rates in range from 1 to 5", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        long petId = 1L;
        long reservationId = 1L;
        BigDecimal rate = BigDecimal.valueOf(6);
        String comment = "";
        PetRateRequestDto petRateRequestDto = new PetRateRequestDto(petId, reservationId, rate, comment);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.checkIfRateForPetAndReservationExists(petId, reservationId)).willReturn(false);
        given(reservationService.isReservationCompleted(reservationId)).willReturn(true);

        // when
        ResponseEntity<String> result = petController.addPetRate(petRateRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_whenRateIsLowerThenOne() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Only rates in range from 1 to 5", HttpStatus.BAD_REQUEST);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        long petId = 1L;
        long reservationId = 1L;
        BigDecimal rate = BigDecimal.valueOf(-1);
        String comment = "";
        PetRateRequestDto petRateRequestDto = new PetRateRequestDto(petId, reservationId, rate, comment);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.checkIfRateForPetAndReservationExists(petId, reservationId)).willReturn(false);
        given(reservationService.isReservationCompleted(reservationId)).willReturn(true);

        // when
        ResponseEntity<String> result = petController.addPetRate(petRateRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

    @Test
    void addPetRate_shouldAddRate() {
        // given
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        Role role = new Role();
        role.setName(RoleEnum.ROLE_HOTEL.name());
        User user = new User();
        user.setRoles(Set.of(role));
        long petId = 1L;
        long reservationId = 1L;
        BigDecimal rate = BigDecimal.ONE;
        String comment = "";
        PetRateRequestDto petRateRequestDto = new PetRateRequestDto(petId, reservationId, rate, comment);
        given(userService.getCurrentUser()).willReturn(user);
        given(petService.checkIfRateForPetAndReservationExists(petId, reservationId)).willReturn(false);
        given(reservationService.isReservationCompleted(reservationId)).willReturn(true);
        doNothing().when(petService).addPetRate(petRateRequestDto, user);

        // when
        ResponseEntity<String> result = petController.addPetRate(petRateRequestDto);

        // then
        assertEquals(responseEntity, result);
    }

}