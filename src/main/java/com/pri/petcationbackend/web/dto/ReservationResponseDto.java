package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponseDto {

    private Long id;
    private Date from;
    private Date to;
    private Boolean isTrial;
    private PetDto petDto;
    private RoomDto roomDto;
    private BigDecimal price;
}
