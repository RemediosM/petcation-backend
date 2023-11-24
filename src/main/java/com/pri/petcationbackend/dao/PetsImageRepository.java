package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.PetsImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetsImageRepository extends JpaRepository<PetsImage, Long> {

    List<PetsImage> findAllByPet(Pet pet);

}
