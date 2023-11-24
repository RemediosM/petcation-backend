package com.pri.petcationbackend.service;

import com.pri.petcationbackend.web.dto.PetDto;
import com.pri.petcationbackend.model.User;

import java.util.List;

public interface PetService {
    List<PetDto> getAllPetsByUser(User user);

    List<PetDto> getAllPets();

    PetDto addModifyPet(PetDto petDto, User currentUser);

    void delete(Long id);
}
