package com.pri.petcationbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue
    @Column(name = "Image_id")
    private Long imageId;
    @Column(name = "Image_path")
    private String path;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Pet_id")
    private Pet pet;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Hotel_id")
    private Hotel hotel;
}
