package com.pri.petcationbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="pet_types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetType {

    @Id
    @GeneratedValue
    @Column(name = "Pet_type_id")
    private Long petTypeId;
    @NotNull
    @NotEmpty
    @Column(name = "Name")
    private String name;
    @ManyToMany(mappedBy = "petTypes")
    Set<Room> rooms;

    public PetType(String name) {
        this.name = name;
    }
}
