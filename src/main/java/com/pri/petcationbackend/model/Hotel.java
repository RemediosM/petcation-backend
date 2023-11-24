package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.HotelDto;
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
    @Column(name = "Active")
    private Boolean isActive;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "User_id")
    private User user;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Address_id")
    private Address address;

    public HotelDto toDto() {
        return HotelDto.builder()
                .id(hotelId)
                .name(name)
                .addressDto(address != null ? address.toDto() : null)
                .build();
    }
}
