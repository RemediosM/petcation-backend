package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.*;
import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.PetOwner;
import com.pri.petcationbackend.model.PetType;
import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.web.dto.PetResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private PetOwnerRepository petOwnerRepository;
    @Mock
    private PetTypeRepository petTypeRepository;
    @Mock
    private PetRateRepository petRateRepository;
    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    void getAllPetsByUserWhenUserNull() {
        // when
        var result = petService.getAllPetsByUser(null);

        // then
        assertNull(result);
    }

    @Test
    void getAllPetsByUserWhenNothingFound() {
        // given
        var user = new User();
        given(petRepository.findAllByUser(user)).willReturn(new HashSet<>());

        // when
        var result = petService.getAllPetsByUser(user);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPetsByUserWhenPetsFound() {
        // given
        var user = new User();
        var pet = new Pet(1L, LocalDate.now(), "name1", "description1", new PetOwner(), new PetType("DOG"), new ArrayList<>(), new ArrayList<>());
        var petDto = new PetResponseDto(pet, Collections.emptyList());
        given(petRepository.findAllByUser(user)).willReturn(Set.of(pet));

        // when
        var result = petService.getAllPetsByUser(user);

        // then
        assertEquals(1, result.size());
        assertEquals(petDto, result.get(0));
    }

    @Test
    void getAllPetsWhenNothingFound() {
        // given
        given(petRepository.findAll()).willReturn(new ArrayList<>());

        // when
        var result = petService.getAllPets();

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPetsWhenPetsFound() {
        // given
        var user = new User();
        var pet = new Pet(1L, LocalDate.now(), "name1", "description1", new PetOwner(), new PetType("DOG"), new ArrayList<>(), new ArrayList<>());
        var petDto = new PetResponseDto(pet, Collections.emptyList());
        given(petRepository.findAll()).willReturn(List.of(pet));

        // when
        var result = petService.getAllPets();

        // then
        assertEquals(1, result.size());
        assertEquals(petDto, result.get(0));
    }

}