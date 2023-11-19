package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetTypeRepository extends JpaRepository<PetType, Long> {

    Optional<PetType> findByName(String name);
}
