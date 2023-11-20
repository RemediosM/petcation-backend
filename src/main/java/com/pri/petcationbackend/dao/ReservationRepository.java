package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Hotel;
import com.pri.petcationbackend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r left join r.room ro where ro.hotel = :hotel")
    List<Reservation> findAllByHotel(Hotel hotel);
}
