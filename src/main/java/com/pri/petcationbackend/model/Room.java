package com.pri.petcationbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name="rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    @Column(name = "Room_id")
    private Long roomId;
    @Column(name = "Price")
    private BigDecimal price;
    @Column(name = "Beds")
    private Integer beds;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Hotel_id")
    private Hotel hotel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rooms_pet_types",
            joinColumns = @JoinColumn(name = "Room_id"),
            inverseJoinColumns = @JoinColumn(name = "Pet_type_id"))
    private Set<PetType> petTypes;
}
