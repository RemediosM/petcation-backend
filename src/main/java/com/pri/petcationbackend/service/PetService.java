package com.pri.petcationbackend.service;

import com.pri.petcationbackend.web.dto.PetDto;
import com.pri.petcationbackend.model.User;

import java.util.List;

public interface PetService {
    List<PetDto> getAllPetsByUser();
    List<PetDto> getAllPetsByUser(User user);
    PetDto addModifyPet(PetDto petDto);

    void delete(Long id);
}
