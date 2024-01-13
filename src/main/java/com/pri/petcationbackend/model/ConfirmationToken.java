package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.ConfirmationTokenDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="confirmation_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Confirmation_token_id")
    private Long confirmationTokenId;

    @Column(name="Token")
    private String token;

    @Column(name="Created_date")
    private LocalDateTime createdDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = LocalDateTime.now();
        token = UUID.randomUUID().toString();
    }

    public ConfirmationTokenDto toDto() {
        return ConfirmationTokenDto.builder()
                .id(confirmationTokenId)
                .token(token)
                .createdDate(createdDate)
                .userId(user != null ? user.getUserId() : null)
                .build();
    }

}
