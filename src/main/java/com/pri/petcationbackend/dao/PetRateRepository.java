package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.PetRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRateRepository extends JpaRepository<PetRate, Long> {

    List<PetRate> findAllByPet(Pet pet);
}
