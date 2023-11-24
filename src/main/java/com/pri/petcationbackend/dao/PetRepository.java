package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.PetOwner;
import com.pri.petcationbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Set<Pet> findAllByPetOwner(PetOwner petOwner);

    @Query("select p from Pet p left join p.petOwner po where po.user = :user")
    Set<Pet> findAllByUser(User user);
}
