package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.*;
import com.pri.petcationbackend.model.*;
import com.pri.petcationbackend.web.dto.AddressDto;
import com.pri.petcationbackend.web.dto.SignUpDto;
import com.pri.petcationbackend.web.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;
    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final PetTypeRepository petTypeRepository;

    private final PetService petService;


    @Override
    public void registerNewUserAccount(SignUpDto signUpDto)  {
        Address address = null;
        AddressDto addressDto = signUpDto.getAddressDto();

        if(addressDto != null && StringUtils.isNotEmpty(addressDto.getCountry()) && StringUtils.isNotEmpty(addressDto.getCity())
            && StringUtils.isNotEmpty(addressDto.getStreet())) {
            Country country = countryRepository.findByName(addressDto.getCountry())
                    .orElse(countryRepository.save(new Country(addressDto.getCountry())));
            City city = cityRepository.findByNameAndCountry(addressDto.getCity(), country)
                    .orElse(cityRepository.save(new City(addressDto.getCity(), country)));
            address = addressRepository.save(Address.builder()
                    .phone(addressDto.getPhoneNumber())
                    .street(addressDto.getStreet())
                    .city(city)
                    .build());
        }

        User user = User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .enabled(true)
                .address(address)
                .build();
        roleRepository.findByName("ROLE_USER").ifPresent(role ->
            user.setRoles(Set.of(role)));
        userRepository.save(user);
        PetOwner petOwner = petOwnerRepository.save(new PetOwner(user));


        signUpDto.getPets().stream()
                .filter(petDto -> petDto.getName() != null && petDto.getPetType() != null)
                .forEach(pet -> {
                    PetType petType = petTypeRepository.findByName(pet.getPetType().name())
                                    .orElse(petTypeRepository.save(new PetType(pet.getPetType().name())));
            petRepository.save(new Pet(petOwner, pet.getAge(), petType, pet.getName(), pet.getDescription()));

                }
                );
    }

    @Override
    public User findByEmail(String email) {
        return StringUtils.isNotEmpty(email) ? userRepository.findByEmail(email) : null;
    }

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return UserDto.builder()
                .userDetails(new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPassword(), enabled, accountNonExpired,
                        credentialsNonExpired, accountNonLocked, getAuthorities(user.getRoles())))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .pets(petService.getAllPetsByUser(user))
                .addressDto(user.getAddress() != null ? user.getAddress().toDto() : null)
                .build();
    }

    private static Set<GrantedAuthority> getAuthorities (Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

}
