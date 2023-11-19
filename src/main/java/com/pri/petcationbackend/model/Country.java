package com.pri.petcationbackend.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Entity(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Country {
    @Id
    @GeneratedValue
    @Column(name = "Country_id")
    private Long countryId;
    private String name;

    public Country(String name) {
        this.name = name;
    }
}
