package com.pri.petcationbackend.web.dto;

import lombok.*;

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
}
