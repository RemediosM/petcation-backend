package com.pri.petcationbackend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PetDto {
    private Long id;
    private String name;
    private String description;
    private Integer age;
    private PetTypeEnum petType;
}
