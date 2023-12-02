package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class PetDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate birthDate;
    private PetTypeEnum petType;
    private List<PetsImageDto> images;
    private PetOwnerDto petOwnerDto;
}
