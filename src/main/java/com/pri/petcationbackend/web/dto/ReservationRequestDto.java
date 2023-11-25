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
    private Boolean isTrial;
    private List<PetRoomDto> petRoomDtos;
    private Long roomId;
}
