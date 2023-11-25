package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.HotelRoomDto;
import com.pri.petcationbackend.web.dto.RoomDto;
import com.pri.petcationbackend.web.dto.RoomHotelDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservation;

    public RoomDto toDto() {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(roomId);
        roomDto.setPrice(price);
        roomDto.setHotelDto(hotel != null ? new HotelRoomDto(hotel) : null);
        roomDto.setPetTypes(petTypes != null ? petTypes.stream().map(PetType::getName).collect(Collectors.toSet()) : null);
        return roomDto;
    }

    public RoomHotelDto toRoomHotelDto() {
        RoomHotelDto roomDto = new RoomHotelDto();
        roomDto.setId(roomId);
        roomDto.setPrice(price);
        roomDto.setPetTypes(petTypes != null ? petTypes.stream().map(PetType::getName).collect(Collectors.toSet()) : null);
        return roomDto;
    }
}
