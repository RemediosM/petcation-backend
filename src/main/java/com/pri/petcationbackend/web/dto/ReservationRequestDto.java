package com.pri.petcationbackend.web.dto;

import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.Reservation;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequestDto {

    private LocalDate from;
    private LocalDate to;
    private List<Long> petIds;
    private Long hotelId;

    public ReservationRequestDto(Reservation reservation) {
        this.from = reservation.getFrom();
        this.to = reservation.getTo();
        this.hotelId = reservation.getHotel() != null ? reservation.getHotel().getHotelId() : null;
        this.petIds = CollectionUtils.emptyIfNull(reservation.getPets()).stream().map(Pet::getPetId).toList();
    }
}
