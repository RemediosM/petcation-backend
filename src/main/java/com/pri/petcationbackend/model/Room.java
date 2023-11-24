package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.RoomDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Hotel_id")
    private Hotel hotel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rooms_pet_types",
            joinColumns = @JoinColumn(name = "Room_id"),
            inverseJoinColumns = @JoinColumn(name = "Pet_type_id"))
    private Set<PetType> petTypes;

    public RoomDto toDto() {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(roomId);
        roomDto.setPrice(price);
        roomDto.setHotelDto(hotel != null ? hotel.toDto() : null);
        roomDto.setPetTypes(petTypes != null ? petTypes.stream().map(PetType::getName).collect(Collectors.toSet()) : null);
        return roomDto;
    }
}
