package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Reservation;
import com.pri.petcationbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r left join r.hotel h where h.user = :user")
    List<Reservation> findAllByHotelUser(User user);

    @Query("select r from Reservation r left join r.petOwner p where p.user = :user")
    List<Reservation> findAllByPetOwnerUser(User user);

    @Query("select max(r.to) from Reservation r left join r.rooms ro where ro.hotel.hotelId = :id and r.status = 2")
    LocalDate findLastReservationForHotel(Long id);

    @Query(
            value = "SELECT MAX(r.Reservation_number) FROM reservations r",
            nativeQuery = true)
    Integer findMaxReservationNumber();

    List<Reservation> findAllByHotelHotelId(Long id);
}
