package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.PetOwner;
import com.pri.petcationbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {
    Optional<PetOwner> findByUser(User user);
}
