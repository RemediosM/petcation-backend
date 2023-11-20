package com.pri.petcationbackend.web.controller;

import com.pri.petcationbackend.web.dto.HotelDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@Tag(name = "Hotels")
@RequiredArgsConstructor
public class HotelController {

    @GetMapping("/hotels")
    @Operation(summary = "Get hotels.")
    public List<HotelDto> getHotels() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        User user = userService.findByEmail(principal instanceof UserDetails userDetails
//                ? userDetails.getUsername()
//                : principal.toString());

        return   Collections.emptyList();
    }
}
