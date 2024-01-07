package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.*;
import com.pri.petcationbackend.model.*;
import com.pri.petcationbackend.web.dto.PetDto;
import com.pri.petcationbackend.web.dto.PetRateRequestDto;
import com.pri.petcationbackend.web.dto.PetResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PetServiceImpl implements PetService{

    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final PetTypeRepository petTypeRepository;
    private final PetRateRepository petRateRepository;
    private final HotelRepository hotelRepository;
    private final PetsImageRepository petsImageRepository;

    @Override
    public List<PetResponseDto> getAllPetsByUser(User user) {
        if (user == null) {
            return null;
        }

        return petRepository.findAllByUser(user)
                .stream()
                .map(pet -> new PetResponseDto(pet, petRateRepository.findAllByPet(pet)))
                .toList();
    }

    @Override
    public List<PetResponseDto> getAllPets() {
        return petRepository.findAll().stream()
                .map(pet -> new PetResponseDto(pet, petRateRepository.findAllByPet(pet)))
                .toList();
    }

    @Override
    public void addModifyPet(PetDto petDto, User currentUser) {
        if(currentUser != null) {
            petOwnerRepository.findByUser(currentUser)
                    .ifPresent(petOwner -> {
                        PetType petType = petTypeRepository.findByName(petDto.getPetType().name()).orElse(null);
                        petRepository.save(new Pet(petOwner, Objects.requireNonNullElseGet(petType, () -> petTypeRepository.save(new PetType(petDto.getPetType().name()))), petDto));
                    });

        }
    }

    @Override
    public void delete(Long id) {
        petRepository.findById(id).ifPresent(p -> petRepository.deleteById(id));
    }

    @Override
    public void addPetRate(PetRateRequestDto petRateRequestDto, User currentUser) {
        if(currentUser != null) {
            hotelRepository.findByUser(currentUser)
                    .ifPresent(hotel -> petRateRepository.save(PetRate.builder()
                            .pet(petRepository.findById(petRateRequestDto.getPetId()).orElse(null))
                            .hotel(hotel)
                            .rate(petRateRequestDto.getRate().doubleValue())
                            .comment(petRateRequestDto.getComment())
                            .build()));
        }
    }

    @Override
    public Pet findPetById(Long id) {
        if(id != null) {
            return petRepository.findById(id).orElse(null);
        }
        return null;
    }

    @Override
    public void addImagesForPet(Pet pet, List<String> imageUrls) {
        List<PetsImage> newPetsImages = imageUrls.stream().map(image -> new PetsImage(image, pet))
                .toList();
        petsImageRepository.saveAll(newPetsImages);
    }

    @Override
    public void deleteImage(Long imageId) {
        if(imageId != null) {
            petsImageRepository.deleteById(imageId);
        }
    }
}
