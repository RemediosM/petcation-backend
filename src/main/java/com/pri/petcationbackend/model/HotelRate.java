package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.HotelRateDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "hotel_rates")
@Builder
public class HotelRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Hotel_rates_id")
    private Long hotelRateId;
    private Double rate;
    private String comment;
    @ManyToOne
    @JoinColumn(name="Hotel_id", nullable=false)
    private Hotel hotel;
    @ManyToOne
    @JoinColumn(name="Pet_owner_id", nullable=false)
    private PetOwner petOwner;

    public HotelRateDto toDto() {
        return new HotelRateDto(BigDecimal.valueOf(rate).setScale(2, RoundingMode.HALF_UP), comment, petOwner.toDto());
    }
}
