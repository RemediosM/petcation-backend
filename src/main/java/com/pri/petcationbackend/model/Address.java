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
    private String coordinates;

    private String phone;

    @ManyToOne
    @JoinColumn(name="City_id", nullable=false)
    private City city;

    @Override
    public String toString() {
        return "Addresses{" +
                "id=" + addressId +
                ", street='" + street + '\'' +
                ", coordinates='" + coordinates + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public AddressDto toDto() {
        if(city != null && city.getCountry() != null) {
            return new AddressDto(phone, street, city.getName(), city.getCountry().getName());
        }
        return null;
    }
}
