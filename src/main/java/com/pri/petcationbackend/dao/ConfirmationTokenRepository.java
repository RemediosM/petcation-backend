package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.ConfirmationToken;
import com.pri.petcationbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    
    ConfirmationToken findByToken(String token);
    ConfirmationToken findByUser(User user);
}
