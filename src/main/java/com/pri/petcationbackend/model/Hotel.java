package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.HotelDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="hotels")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Hotel_id")
    private Long hotelId;
    @Column(name = "Name")
    private String name;
    @Column(name = "Description")
    private String description;
    @Column(name = "Active")
    private Boolean isActive;
    @OneToOne
    @JoinColumn(name = "User_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "Address_id")
    private Address address;

    @OneToMany(mappedBy = "hotel" )
    private List<HotelsImage> images;

    @OneToMany(mappedBy = "hotel" )
    private List<Room> rooms;


    public HotelDto toDto() {
        return HotelDto.builder()
                .id(hotelId)
                .name(name)
                .addressDto(address != null ? address.toDto() : null)
                .description(description)
                .images(images.stream().map(HotelsImage::toDto).toList())
                .rooms(rooms.stream().map(Room::toRoomHotelDto).toList())
                .build();
    }
}
