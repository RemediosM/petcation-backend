package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.service.PetService;
import com.pri.petcationbackend.service.ReservationService;
import com.pri.petcationbackend.service.UserService;
import com.pri.petcationbackend.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Tag(name = "Pets")
@RequiredArgsConstructor
@CrossOrigin
public class PetController {
    private final PetService petService;
    private final UserService userService;

    private final ReservationService reservationService;

    @GetMapping("/usersPets")
    @Operation(summary = "Get pets for user.")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<PetResponseDto> getPetsForUser() {
        return  petService.getAllPetsByUser(userService.getCurrentUser());
    }

    @GetMapping("/pets")
    @Operation(summary = "Get all pets.")
    public List<PetResponseDto> getPets() {
        return  petService.getAllPets();
    }

    @PostMapping("/addModifyPet")
    @Operation(summary = "Add or modify pet.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> addPet(@RequestBody @Valid PetDto petDto) {

        if(StringUtils.isEmpty(petDto.getName()))
            return new ResponseEntity<>("Name is empty!", HttpStatus.BAD_REQUEST);

        if(petDto.getPetType() == null)
            return new ResponseEntity<>("Type is empty!", HttpStatus.BAD_REQUEST);

        petService.addModifyPet(petDto, userService.getCurrentUser());

        return new ResponseEntity<>(petDto.getId() != null
                ? "Pet is modified successfully!"
                : "Pet is added successfully!", HttpStatus.OK);
    }

    @PostMapping("/addImages")
    @Operation(summary = "Add pets images.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> addPetsImages(@RequestBody @Valid PetImagesDto petImagesDto) {


        if(petImagesDto.getPetId() == null || CollectionUtils.isEmpty(petImagesDto.getImageUrls()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = userService.getCurrentUser();
        Pet pet = petService.findPetById(petImagesDto.getPetId());

        if(user.getRoles().stream().anyMatch(role -> !RoleEnum.ROLE_USER.name().equals(role.getName())) || pet == null
                || pet.getPetOwner() == null
                || !user.equals(pet.getPetOwner().getUser())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        petService.addImagesForPet(pet, petImagesDto.getImageUrls());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteImage")
    @Operation(summary = "Delete image.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteImage(@RequestParam(value = "imageId") Long imageId) {
        petService.deleteImage(imageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deletePet")
    @Operation(summary = "Delete pet.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteById(@RequestParam(value = "id") Long id) {
        petService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addRate")
    @Operation(summary = "Add rate for pet.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> addPetRate(@RequestBody @Valid PetRateRequestDto petRateRequestDto) {
        User user = userService.getCurrentUser();
        if(user.getRoles().stream().noneMatch(role -> RoleEnum.ROLE_HOTEL.name().equals(role.getName()))){
            return new ResponseEntity<>("Only hotel can add rate for pet", HttpStatus.BAD_REQUEST);
        }

        if(petRateRequestDto == null || petRateRequestDto.getRate() == null || BigDecimal.ZERO.equals(petRateRequestDto.getRate()))
            return new ResponseEntity<>("Rate is empty!", HttpStatus.BAD_REQUEST);

        if(petRateRequestDto.getPetId() == null)
            return new ResponseEntity<>("Pet is empty!", HttpStatus.BAD_REQUEST);

        Long reservationId = petRateRequestDto.getReservationId();
        if(reservationId == null)
            return new ResponseEntity<>("Reservation is empty!", HttpStatus.BAD_REQUEST);

        if(petService.checkIfRateForPetAndReservationExists(petRateRequestDto.getPetId(), reservationId) || !reservationService.isReservationCompleted(reservationId)) {
            return new ResponseEntity<>("There are no reservations to rate", HttpStatus.BAD_REQUEST);
        }

        if(petRateRequestDto.getRate().compareTo(BigDecimal.valueOf(5)) > 0 || petRateRequestDto.getRate().compareTo(BigDecimal.valueOf(1)) < 0)
            return new ResponseEntity<>("Only rates in range from 1 to 5", HttpStatus.BAD_REQUEST);

        petService.addPetRate(petRateRequestDto, userService.getCurrentUser());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
