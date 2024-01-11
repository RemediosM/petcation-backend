package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.HotelRoomDto;
import com.pri.petcationbackend.web.dto.PetTypeEnum;
import com.pri.petcationbackend.web.dto.RoomDto;
import com.pri.petcationbackend.web.dto.RoomHotelDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "Hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "Pet_type_id")
    private PetType petType;

    @ManyToMany(mappedBy = "rooms")
    private List<Reservation> reservations;

    public RoomDto toDto() {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(roomId);
        roomDto.setPrice(price);
        roomDto.setHotelDto(hotel != null ? new HotelRoomDto(hotel) : null);
        roomDto.setPetType(petType != null ? PetTypeEnum.valueOf(petType.getName()) : null);
        return roomDto;
    }

    public RoomHotelDto toRoomHotelDto() {
        RoomHotelDto roomDto = new RoomHotelDto();
        roomDto.setId(roomId);
        roomDto.setPrice(price);
        roomDto.setPetType(petType != null ? PetTypeEnum.valueOf(petType.getName()) : null);
        return roomDto;
    }
}
