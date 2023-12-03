package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.PetDto;
import com.pri.petcationbackend.web.dto.PetTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="pets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Pet_id")
    private Long petId;

    @Column(name = "Birth_date")
    private LocalDate birthDate;

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
    @ManyToMany(mappedBy = "pets")
    private List<Reservation> reservations;

    public Pet(PetOwner petOwner, LocalDate birthDate, PetType petType, String name, String description) {
        this.petOwner = petOwner;
        this.birthDate = birthDate;
        this.petType = petType;
        this.name = name;
        this.description = description;
    }

    public Pet(PetOwner petOwner, PetType petType, PetDto petDto) {
        this.petOwner = petOwner;
        this.petId = petDto.getId();
        this.name = petDto.getName();
        this.birthDate = petDto.getBirthDate();
        this.description = petDto.getDescription();
        this.petType = petType;
    }

    public PetDto toDto() {
        return PetDto.builder()
                .id(petId)
                .name(name)
                .birthDate(birthDate)
                .petType(PetTypeEnum.valueOf(petType.getName()))
                .petOwnerDto(petOwner.toDto())
                .description(description)
                .images(petsImages.stream().map(PetsImage::toDto).toList())
                .build();
    }
}
