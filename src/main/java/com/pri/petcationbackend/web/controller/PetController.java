package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.service.PetService;
import com.pri.petcationbackend.service.UserService;
import com.pri.petcationbackend.web.dto.PetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Pets")
@RequiredArgsConstructor
@CrossOrigin
public class PetController {
    private final PetService petService;
    private final UserService userService;

    @GetMapping("/usersPets")
    @Operation(summary = "Get pets for user.")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<PetDto> getPetsForUser() {
        return  petService.getAllPetsByUser(userService.getCurrentUser());
    }

    @GetMapping("/pets")
    @Operation(summary = "Get all pets.")
    public List<PetDto> getPets() {
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

    @DeleteMapping("/deletePet")
    @Operation(summary = "Delete pet.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteById(@RequestParam(value = "id") Long id) {
        petService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
