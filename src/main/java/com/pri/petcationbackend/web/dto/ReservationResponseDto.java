package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponseDto {

    private Long id;
    private LocalDate from;
    private LocalDate to;
    private Boolean isTrial;
    private PetDto petDto;
    private RoomDto roomDto;
    private BigDecimal price;
}
