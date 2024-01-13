package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    
    ConfirmationToken findByToken(String token);
}
