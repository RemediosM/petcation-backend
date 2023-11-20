package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
