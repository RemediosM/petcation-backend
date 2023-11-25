package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.ReservationResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Reservation_id")
    private Long reservationId;
    @Column(name = "Reservation_number")
    private Long reservationNo;
    @Column(name = "Date_from")
    private LocalDate from;
    @Column(name = "Date_to")
    private LocalDate to;
    @Column(name = "Trial")
    private Boolean isTrial;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Room_id")
    private Room room;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Pet_id")
    private Pet pet;

    public ReservationResponseDto toDto() {
        return ReservationResponseDto.builder()
                .id(reservationId)
                .from(from)
                .to(to)
                .price(calculatePrice())
                .isTrial(isTrial)
                .roomDto(room != null ? room.toDto() : null)
                .petDto(pet != null ? pet.toDto() : null)
                .build();
    }

    private BigDecimal calculatePrice() {
        long dateDiff = ChronoUnit.DAYS.between(to, from);
        if(dateDiff > 0 && room != null && room.getPrice() != null) {
            return room.getPrice().multiply(BigDecimal.valueOf(dateDiff)).setScale(2, RoundingMode.HALF_UP);
        }
        return null;
    }
}
