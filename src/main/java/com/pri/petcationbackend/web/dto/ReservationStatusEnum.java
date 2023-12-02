package com.pri.petcationbackend.web.dto;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReservationStatusEnum {

    PENDING(1),
    ACCEPTED(2),
    REJECTED(3),
    DELETED(4);

    private Integer code;

    ReservationStatusEnum(Integer code) {
        this.code = code;
    }

    public static ReservationStatusEnum getFromCode(Integer code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code))
                .findFirst().orElse(null);
    }
}
