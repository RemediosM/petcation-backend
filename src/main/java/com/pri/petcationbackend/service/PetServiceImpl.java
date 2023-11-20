package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.PetTypeRepository;
import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.PetType;
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
    private final UserService userService;
    private final PetTypeRepository petTypeRepository;

    @Override
    public List<PetDto> getAllPetsByUser() {
        User user = userService.getCurrentUser();
        return getAllPetsByUser(user);
    }

    @Override
    public List<PetDto> getAllPetsByUser(User user) {
        if (user == null) return null;
        List<PetDto> petDtos = new ArrayList<>();
        petOwnerRepository.findByUser(user)
                .ifPresent(petOwner -> petRepository.findAllByPetOwner(petOwner)
                        .forEach(pet ->
                                petDtos.add(new PetDto(pet.getPetId(), pet.getName(), pet.getDescription(), pet.getAge(),
                                        PetTypeEnum.valueOf(pet.getPetType().getName()))))
                );
        return petDtos;
    }

    @Override
    public PetDto addModifyPet(PetDto petDto) {
        User user = userService.getCurrentUser();
        if(user != null) {
            petOwnerRepository.findByUser(user)
                    .ifPresent(petOwner -> {
                        PetType petType = petTypeRepository.findByName(petDto.getPetType().name())
                                .orElse(petTypeRepository.save(new PetType(petDto.getPetType().name())));

                        petRepository.save(new Pet(petOwner, petType, petDto));
                    });

        }
        return null;
    }

    @Override
    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}
