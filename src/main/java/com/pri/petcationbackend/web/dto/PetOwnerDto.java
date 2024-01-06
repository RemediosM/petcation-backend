package com.pri.petcationbackend.web.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class PetOwnerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private AddressDto addressDto;
}
