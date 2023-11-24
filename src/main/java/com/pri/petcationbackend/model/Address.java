package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.AddressDto;
import lombok.*;
import jakarta.persistence.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "addresses")
@Builder
public class Address {

    @Id
    @GeneratedValue
    @Column(name = "Address_id")
    private Long addressId;
    private String street;
    private Double latitude;
    private Double longitude;
    private String phone;
    @ManyToOne
    @JoinColumn(name="City_id", nullable=false)
    private City city;

    public AddressDto toDto() {
        if(city != null && city.getCountry() != null) {
            return new AddressDto(phone, street, city.getName(), city.getCountry().getName());
        }
        return null;
    }
}
