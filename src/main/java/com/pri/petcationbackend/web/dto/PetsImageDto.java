package com.pri.petcationbackend.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PetsImageDto {
    private Long id;
    private String url;
}
