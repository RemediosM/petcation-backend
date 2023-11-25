package com.pri.petcationbackend.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyUserDto {

    private String firstName;
    private String lastName;
    private AddressDto addressDto;
}
