package com.pri.petcationbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name="reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "Reservation_id")
    private Long reservationId;
    @Column(name = "Date_from")
    private Date from;
    @Column(name = "Date_to")
    private Date to;
    @Column(name = "Trial")
    private Boolean isTrial;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Pet_owner_id")
    private PetOwner petOwner;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Room_id")
    private Room room;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservations_pets",
            joinColumns = @JoinColumn(name = "Reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "Pet_id"))
    private Set<Pet> pets;
}
