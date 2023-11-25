package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.HotelsImageDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="hotels_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelsImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Hotels_image_id")
    private Long petsImageId;
    @Column(name = "Image_path")
    private String path;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Hotel_id")
    private Hotel hotel;

    public HotelsImageDto toDto() {
        return new HotelsImageDto(petsImageId, path);
    }
}
