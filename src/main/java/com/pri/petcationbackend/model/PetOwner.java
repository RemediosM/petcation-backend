package com.pri.petcationbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="pet_owners")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetOwner {

    @Id
    @GeneratedValue
    @Column(name = "Pet_owner_id")
    private Long petOwnerId;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public PetOwner(User user) {
        this.user = user;
    }
}
