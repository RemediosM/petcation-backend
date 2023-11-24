package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h WHERE h.isActive IS NULL or h.isActive")
    List<Hotel> findAllActiveHotels();
}
