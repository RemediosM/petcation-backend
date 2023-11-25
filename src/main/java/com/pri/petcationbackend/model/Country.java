package com.pri.petcationbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Country_id")
    private Long countryId;
    private String name;

    public Country(String name) {
        this.name = name;
    }
}
