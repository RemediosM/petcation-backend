package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private ReservationStatusEnum status;
    private List<PetDto> petDtos;
    private List<RoomHotelDto> roomDtos;
    private BigDecimal totalPrice;
}
