package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequestDto {

    private Date from;
    private Date to;
    private Boolean isTrial;
    private Long petId;
    private Long roomId;
}
