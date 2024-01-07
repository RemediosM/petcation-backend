package com.pri.petcationbackend.model;

import com.pri.petcationbackend.web.dto.ReservationResponseDto;
import com.pri.petcationbackend.web.dto.ReservationStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name="reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Reservation_id")
    private Long reservationId;
    @Column(name = "Reservation_number")
    private Long reservationNo;
    @Column(name = "Date_from")
    private LocalDate from;
    @Column(name = "Date_to")
    private LocalDate to;
    @Column(name = "Trial")
    private Boolean isTrial;
    @Column(name = "Hotel_rate")
    private Boolean isAnyRateForHotel;
    @Column(name = "Status")
    private Integer status;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Hotel_id")
    private Hotel hotel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Pet_owner_id")
    private PetOwner petOwner;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservations_rooms",
            joinColumns = @JoinColumn(name = "Reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "Room_id"))
    private List<Room> rooms;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservations_pets",
            joinColumns = @JoinColumn(name = "Reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "Pet_id"))
    private List<Pet> pets;

    public ReservationResponseDto toDto() {
        return ReservationResponseDto.builder()
                .id(reservationId)
                .from(from)
                .to(to)
                .status(ReservationStatusEnum.getFromCode(status))
                .totalPrice(calculatePrice())
                .isTrial(isTrial)
                .roomDtos(rooms != null ? rooms.stream().map(Room::toRoomHotelDto).toList() : null)
                .petDtos(pets != null ? pets.stream().map(Pet::toDto).toList() : null)
                .hotelDto(hotel != null ? hotel.toDto() : null)
                .build();
    }

    private BigDecimal total(Room room, long dateDiff) {
        return room != null && room.getPrice() != null
                ? room.getPrice().multiply(BigDecimal.valueOf(dateDiff))
                : BigDecimal.ZERO;
    }

    private BigDecimal calculatePrice() {
        long dateDiff = ChronoUnit.DAYS.between(from, to);
        if(dateDiff > 0 && rooms != null) {
            return rooms.stream()
                    .map(room -> total(room, dateDiff))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    public boolean isAccepted() {
        return ReservationStatusEnum.ACCEPTED.getCode().equals(this.status);
    }

    public boolean isPending() {
        return ReservationStatusEnum.PENDING.getCode().equals(this.status);
    }
}
