package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PetTypeQtyDto {

    private String petType;
    private Long qty;
    private BigDecimal price;
}
