package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal age;
    private PetTypeEnum petType;
    private List<PetsImageDto> images;
    private PetOwnerDto petOwnerDto;
}
