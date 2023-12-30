package com.pri.petcationbackend.service;

import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.web.dto.PetDto;
import com.pri.petcationbackend.web.dto.PetRateRequestDto;
import com.pri.petcationbackend.web.dto.PetResponseDto;

import java.util.List;

public interface PetService {
    List<PetResponseDto> getAllPetsByUser(User user);

    List<PetResponseDto> getAllPets();

    void addModifyPet(PetDto petDto, User currentUser);

    void delete(Long id);

    void addPetRate(PetRateRequestDto petRateRequestDto, User currentUser);
}
