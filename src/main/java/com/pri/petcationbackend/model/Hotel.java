package com.pri.petcationbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="hotels")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue
    @Column(name = "Hotel_id")
    private Long hotelId;
    @Column(name = "Name")
    private String name;
    @Column(name = "Description")
    private String description;
    @Column(name = "Inactive")
    private Boolean isInactive;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "User_id")
    private User user;

}
