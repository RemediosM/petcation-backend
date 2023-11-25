package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.PetsImageDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="pets_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetsImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Pets_image_id")
    private Long petsImageId;
    @Column(name = "Image_path")
    private String path;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Pet_id")
    private Pet pet;

    public PetsImageDto toDto() {
        return new PetsImageDto(petsImageId, path);
    }
}
