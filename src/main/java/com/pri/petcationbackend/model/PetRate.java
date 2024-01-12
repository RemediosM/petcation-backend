package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.HotelRoomDto;
import com.pri.petcationbackend.web.dto.PetRateDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "pet_rates")
@Builder
public class PetRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Pet_rates_id")
    private Long petRateId;
    private Double rate;
    private String comment;
    @ManyToOne
    @JoinColumn(name="Hotel_id", nullable=false)
    private Hotel hotel;
    @ManyToOne
    @JoinColumn(name="Pet_id", nullable=false)
    private Pet pet;
    @ManyToOne
    @JoinColumn(name="Reservation_id")
    private Reservation reservation;

    public PetRateDto toDto() {
        return new PetRateDto(BigDecimal.valueOf(rate).setScale(2, RoundingMode.HALF_UP), comment, new HotelRoomDto(hotel));
    }
}
