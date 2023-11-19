package com.pri.petcationbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Entity(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class City {

    @Id
    @GeneratedValue
    @Column(name = "City_id")
    private Long cityId;
    private String name;

    @ManyToOne
    @JoinColumn(name="Country_id", nullable=false)
    private Country country;

    public City(String name, Country country) {
        this.name = name;
        this.country = country;
    }
}
