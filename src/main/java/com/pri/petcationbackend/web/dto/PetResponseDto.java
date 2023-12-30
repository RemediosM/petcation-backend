package com.pri.petcationbackend.web.dto;

import com.pri.petcationbackend.model.Pet;
import com.pri.petcationbackend.model.PetRate;
import com.pri.petcationbackend.model.PetsImage;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class PetResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate birthDate;
    private PetTypeEnum petType;
    private List<PetsImageDto> images;
    private PetOwnerDto petOwnerDto;
    private BigDecimal averageRate;
    private List<PetRateDto> rates;

    public PetResponseDto(Pet pet, List<PetRate> petRates) {
        if(pet != null) {
            this.id = pet.getPetId();
            this.name = pet.getName();
            this.birthDate = pet.getBirthDate();
            this.petType = PetTypeEnum.valueOf(pet.getPetType().getName());
            this.images = CollectionUtils.emptyIfNull(pet.getPetsImages()).stream().map(PetsImage::toDto).toList();
            this.petOwnerDto = pet.getPetOwner().toDto();
        }
        if(petRates != null) {
            rates = petRates.stream().map(PetRate::toDto).toList();
            petRates.stream()
                    .mapToDouble(PetRate::getRate)
                    .average()
                    .ifPresent(d -> averageRate = BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP));

        }
    }
}
