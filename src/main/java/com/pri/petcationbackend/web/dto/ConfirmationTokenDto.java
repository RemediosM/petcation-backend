package com.pri.petcationbackend.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmationTokenDto {

    private Long id;
    private String token;
    private LocalDateTime createdDate;
    private Long userId;
}
