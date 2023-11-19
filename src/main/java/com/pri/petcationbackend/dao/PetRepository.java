package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Set<Pet> findAllByPetOwner(PetOwner petOwner);
}
