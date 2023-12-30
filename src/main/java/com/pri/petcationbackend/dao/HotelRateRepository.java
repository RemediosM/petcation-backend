package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.HotelRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface HotelRateRepository extends JpaRepository<HotelRate, Long> {

    @Query(
            value = "SELECT AVG(h.Rate) FROM hotel_rates h WHERE h.Hotel_id = :id",
            nativeQuery = true)
    Double getAverageRateForHotel(Long id);
}
