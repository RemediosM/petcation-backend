package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.PetDto;
import com.pri.petcationbackend.web.dto.PetTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
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
    private BigDecimal age;

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

    @OneToMany(mappedBy = "pet")
    List<PetsImage> petsImages;

    public Pet(PetOwner petOwner, BigDecimal age, PetType petType, String name, String description) {
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

    public PetDto toDto() {
        return PetDto.builder()
                .id(petId)
                .name(name)
                .age(age)
                .petType(PetTypeEnum.valueOf(petType.getName()))
                .petOwnerDto(petOwner.toDto())
                .description(description)
                .images(petsImages.stream().map(PetsImage::toDto).toList())
                .build();
    }
}
