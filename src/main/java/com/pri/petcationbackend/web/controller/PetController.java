package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.service.PetService;
import com.pri.petcationbackend.service.UserService;
import com.pri.petcationbackend.web.dto.PetDto;
import com.pri.petcationbackend.web.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@Tag(name = "Pets")
@RequiredArgsConstructor
public class PetController {

    private final UserService userService;
    private final PetService petService;

    @GetMapping("/pets")
    @Operation(summary = "Get pets for user.")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<PetDto> getPets() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.findByEmail(principal instanceof UserDetails userDetails
                ? userDetails.getUsername()
                : principal.toString());

        return  user != null ? petService.getAllPetsByUser(user) : Collections.emptyList();
    }

}
