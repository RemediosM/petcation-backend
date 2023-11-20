package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.PetDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="pets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue
    @Column(name = "Pet_id")
    private Long petId;

    @Column(name = "Age")
    private Integer age;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Pet_owner_id")
    private PetOwner petOwner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Pet_type_id")
    private PetType petType;

    @ManyToMany(mappedBy = "pets")
    Set<Reservation> reservations;

    public Pet(PetOwner petOwner, Integer age, PetType petType, String name, String description) {
        this.petOwner = petOwner;
        this.age = age;
        this.petType = petType;
        this.name = name;
        this.description = description;
    }

    public Pet(PetOwner petOwner, PetType petType, PetDto petDto) {
        this.petOwner = petOwner;
        this.petId = petDto.getId();
        this.name = petDto.getName();
        this.age = petDto.getAge();
        this.description = petDto.getDescription();
        this.petType = petType;
    }
}
