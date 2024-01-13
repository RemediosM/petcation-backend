package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.*;
import com.pri.petcationbackend.model.*;
import com.pri.petcationbackend.utils.DistanceUtils;
import com.pri.petcationbackend.web.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final PetTypeRepository petTypeRepository;
    private final PetRepository petRepository;
    private final HotelRateRepository hotelRateRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<HotelDto> getAllHotels() {
        return hotelRepository.findAllActiveHotels().stream()
                .map(hotel -> {
                    HotelDto hotelDto = hotel.toDto();
                    return setAvgRate(hotelDto);
                })
                .toList();
    }

    public List<HotelDto> getHotels(HotelRequestDto hotelRequestDto) {
        return hotelRepository.findAllActiveHotels().stream()
                .filter(hotel -> equalsNullOrZero(hotelRequestDto.getLat()) || equalsNullOrZero(hotelRequestDto.getLon())
                        || DistanceUtils.isTwoPointsInMaxDistance(hotelRequestDto.getMaxDistance(), hotelRequestDto.getLat(),
                        hotelRequestDto.getLon(), hotel.getAddress().getLatitude(), hotel.getAddress().getLongitude()))
                .filter(hotel -> isRoomVacancyWithPetType(hotel.getRooms(), hotelRequestDto.getFrom(), hotelRequestDto.getTo(), hotelRequestDto.getPetTypeQtyDtoList()))
                .map(hotel -> {
                    HotelDto hotelDto = hotel.toDto();
                    return setAvgRate(hotelDto);
                })
                .toList();
    }

    @Override
    public HotelDetailsDto getHotelById(Long id) {

        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotel != null) {
            LocalDate from = LocalDate.now();
            LocalDate to = reservationRepository.findLastReservationForHotel(id);
            boolean isAnyReservation = to != null;
            List<HotelRate> rates = hotelRateRepository.findAllByHotel(hotel);
            OptionalDouble avgRate = rates.stream()
                    .mapToDouble(HotelRate::getRate)
                    .average();
            return new HotelDetailsDto(hotel.getHotelId(), hotel.getName(), hotel.getDescription(), hotel.getAddress().toDto(),
                    isAnyReservation, avgRate.isPresent() ? BigDecimal.valueOf(avgRate.getAsDouble()).setScale(2, RoundingMode.HALF_UP) : null,
                    rates.stream().map(HotelRate::toDto).toList(), getPetTypeQtyList(hotel.getRooms()),
                    isAnyReservation ? getFreeRoomsList(hotel.getRooms(), from, to) : null, org.apache.commons.collections4.CollectionUtils.emptyIfNull(hotel.getImages()).stream().map(HotelsImage::toDto).toList());
        }
        return null;
    }

    private boolean isRoomVacancyWithPetType(List<Room> rooms, LocalDate from, LocalDate to, List<PetTypeQtyDto> petTypeQtyDtoList) {

        if(CollectionUtils.isEmpty(petTypeQtyDtoList)) {
            return rooms.stream()
                    .anyMatch(room -> isPeriodFree(from, to, room.getReservations()));
        }
        for (PetTypeQtyDto petTypeQtyDto : petTypeQtyDtoList)
        {
            List<Room> filteredRooms = rooms.stream()
                    .filter(room -> (petTypeQtyDto.getPetType() == null || room.getPetType().getName().equals(petTypeQtyDto.getPetType()))
                            && isPeriodFree(from, to, room.getReservations()))
                    .toList();
            Long qty = petTypeQtyDto.getQty();
            if (qty == null || filteredRooms.size() >= qty) {
                if(qty != null && qty > 0) {
                    rooms.removeAll(filteredRooms);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private HotelDto setAvgRate(HotelDto hotelDto) {
        Double avgRate = hotelRateRepository.getAverageRateForHotel(hotelDto.getId());
        if(avgRate != null && !avgRate.isNaN()) {
            hotelDto.setAverageRate(BigDecimal.valueOf(avgRate).setScale(2, RoundingMode.HALF_UP));
        }
        return hotelDto;
    }

    private Map<LocalDate, List<PetTypeQtyDto>> getFreeRoomsList(List<Room> rooms, LocalDate from, LocalDate to) {
        Map<LocalDate, List<PetTypeQtyDto>> dateListMap = new TreeMap<>();

        from.datesUntil(to.plusDays(1)).forEach(localDate -> {
            List<PetTypeQtyDto> petTypeQtyDtoList = new ArrayList<>();
            petTypeRepository.findAll().forEach(petType -> {
                List<Room> filteredRooms = rooms.stream()
                        .filter(room -> room.getPetType().getName().equals(petType.getName())
                                && isDayFree(localDate, room.getReservations()))
                        .toList();
                if(!filteredRooms.isEmpty()) {
                    petTypeQtyDtoList.add(new PetTypeQtyDto(petType.getName(), (long) filteredRooms.size(), filteredRooms.get(0).getPrice()));
                }
            });
            if(!petTypeQtyDtoList.isEmpty()) {
                dateListMap.put(localDate, petTypeQtyDtoList);
            }
        });

        return dateListMap;
    }

    @Override
    public List<Room> checkAvailableRooms(ReservationRequestDto reservationDto) {
        Optional<Hotel> hotel = reservationDto.getHotelId() != null ? hotelRepository.findById(reservationDto.getHotelId()) : Optional.empty();
        Map<PetType, Long> petTypeQtyMap = CollectionUtils.isEmpty(reservationDto.getPetIds())
                ? new HashMap<>()
                : petRepository.findAllById(reservationDto.getPetIds()).stream()
                .map(Pet::getPetType)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Room> availableRooms = new ArrayList<>();
        List<Room> rooms = hotel.isPresent() ? hotel.get().getRooms() : new ArrayList<>();
        if(reservationDto.getFrom() != null && reservationDto.getTo() != null) {
            for (Map.Entry<PetType, Long> entry : petTypeQtyMap.entrySet()) {
                List<Room> filteredRooms = rooms.stream()
                        .filter(room -> (entry.getKey() == null || room.getPetType().getName().equals(entry.getKey().getName()))
                                && isPeriodFree(reservationDto.getFrom(), reservationDto.getTo(), room.getReservations()))
                        .toList();
                if (filteredRooms.size() >= entry.getValue()) {
                    rooms.removeAll(filteredRooms);
                    availableRooms.addAll(filteredRooms);
                } else {
                    availableRooms.clear();
                    break;
                }
            }
        }
        return availableRooms;
    }

    @Override
    public void addHotelRate(HotelRateRequestDto hotelRateRequestDto, User currentUser) {
        if(currentUser != null) {
            petOwnerRepository.findByUser(currentUser)
                    .ifPresent(petOwner -> hotelRateRepository.save(HotelRate.builder()
                            .hotel(hotelRepository.findById(hotelRateRequestDto.getHotelId()).orElse(null))
                            .petOwner(petOwner)
                            .rate(hotelRateRequestDto.getRate().doubleValue())
                            .comment(hotelRateRequestDto.getComment())
                            .build()));
        }
    }

    @Override
    public RoomDto findRoomByRoomId(Long id) {
        if(id != null) {
            return roomRepository.findById(id)
                    .map(Room::toDto)
                    .orElse(null);
        }
        return null;
    }

    private List<PetTypeQtyDto> getPetTypeQtyList(List<Room> rooms) {

            List<PetTypeQtyDto> petTypeQtyDtoList = new ArrayList<>();
            petTypeRepository.findAll().forEach(petType -> {
                List<Room> filteredRooms = rooms.stream()
                        .filter(room -> room.getPetType().getName().equals(petType.getName()))
                        .toList();
                if(!filteredRooms.isEmpty()) {
                    petTypeQtyDtoList.add(new PetTypeQtyDto(petType.getName(), (long) filteredRooms.size(), filteredRooms.get(0).getPrice()));
                }
            });

        return petTypeQtyDtoList;
    }

    private boolean isPeriodFree(LocalDate from, LocalDate to, List<Reservation> reservations) {
        return reservations.isEmpty()
                || reservations.stream()
                .noneMatch(Reservation::isAccepted)
                || reservations.stream()
                .anyMatch(reservation -> reservation.isAccepted() && (reservation.getFrom().isBefore(from) || reservation.getFrom().isAfter(to))
                        && (reservation.getTo().isBefore(from) || reservation.getTo().isAfter(to)));
    }

    private boolean isDayFree(LocalDate date, List<Reservation> reservations) {
        return reservations.isEmpty()
                || reservations.stream()
                .noneMatch(Reservation::isAccepted)
                || reservations.stream()
                .filter(Reservation::isAccepted)
                .anyMatch(reservation -> reservation.isAccepted() && !((date.isAfter(reservation.getFrom()) || date.isEqual(reservation.getFrom()))
                        && date.isBefore(reservation.getTo())) || date.isEqual(reservation.getTo()));
    }

    private boolean equalsNullOrZero(Double x) {
        return x == null || x == 0;
    }

}
