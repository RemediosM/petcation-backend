package com.pri.petcationbackend.service;

import com.pri.petcationbackend.web.dto.PetDto;
import com.pri.petcationbackend.web.dto.PetTypeEnum;
import com.pri.petcationbackend.dao.PetOwnerRepository;
import com.pri.petcationbackend.dao.PetRepository;
import com.pri.petcationbackend.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PetServiceImpl implements PetService{

    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;

    @Override
    public List<PetDto> getAllPetsByUser(User user) {
        List<PetDto> petDtos = new ArrayList<>();
        petOwnerRepository.findByUser(user)
                .ifPresent(petOwner -> petRepository.findAllByPetOwner(petOwner)
                        .forEach(pet ->
                                petDtos.add(new PetDto(pet.getName(), pet.getDescription(), pet.getAge(),
                                        PetTypeEnum.valueOf(pet.getPetType().getName()))))
        );
        return petDtos;
    }
}
