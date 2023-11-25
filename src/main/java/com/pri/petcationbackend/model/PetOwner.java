package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.PetOwnerDto;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Pet_owner_id")
    private Long petOwnerId;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public PetOwner(User user) {
        this.user = user;
    }

    public PetOwnerDto toDto() {
        PetOwnerDto petOwnerDto = new PetOwnerDto();
        petOwnerDto.setId(petOwnerId);
        if(user != null) {
            petOwnerDto.setEmail(user.getEmail());
            petOwnerDto.setFirstName(user.getFirstName());
            petOwnerDto.setLastName(user.getLastName());
            petOwnerDto.setAddressDto(user.getAddress() != null ? user.getAddress().toDto() : null);
        }
        return petOwnerDto;
    }
}
