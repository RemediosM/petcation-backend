package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.*;
import com.pri.petcationbackend.model.*;
import com.pri.petcationbackend.web.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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
    private final ConfirmationTokenRepository confirmationTokenRepository;


    @Override
    public ConfirmationTokenDto registerNewUserAccount(SignUpDto signUpDto)  {
        Address address = null;
        AddressDto addressDto = signUpDto.getAddressDto();

        if(addressDto != null && StringUtils.isNotEmpty(addressDto.getCountry()) && StringUtils.isNotEmpty(addressDto.getCity())
                && !addressDto.getCity().equals("none") && !addressDto.getCountry().equals("none")
            && StringUtils.isNotEmpty(addressDto.getStreet())) {
            Country country = getCountryOrAddNew(addressDto.getCountry());
            City city = getCityOrAddNew(addressDto.getCity(), country);
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
                .enabled(false)
                .address(address)
                .build();
        roleRepository.findByName(RoleEnum.ROLE_USER.name()).ifPresent(role ->
            user.setRoles(Set.of(role)));
        userRepository.save(user);
        PetOwner petOwner = petOwnerRepository.save(new PetOwner(user));

        if(signUpDto.getPets() != null) {

            signUpDto.getPets().stream()
                    .filter(petDto -> petDto.getName() != null && petDto.getPetType() != null)
                    .forEach(pet -> {
                                PetType petType = petTypeRepository.findByName(pet.getPetType().name()).orElse(null);
                                if (petType == null) {
                                    petType = petTypeRepository.save(new PetType(pet.getPetType().name()));
                                }
                                petRepository.save(new Pet(petOwner, pet.getBirthDate(), petType, pet.getName(), pet.getDescription()));

                            }
                    );
        }
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken.toDto();
    }

    private City getCityOrAddNew(String city, Country country) {
        return cityRepository.findByNameAndCountry(city, country)
                .orElse(cityRepository.save(new City(city, country)));
    }

    private Country getCountryOrAddNew(String country) {
        return countryRepository.findByName(country)
                .orElse(countryRepository.save(new Country(country)));
    }

    @Override
    public User findByEmail(String email) {
        return StringUtils.isNotEmpty(email) ? userRepository.findByEmailIgnoreCase(email) : null;
    }

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email);
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
                .addressDto(user.getAddress() != null ? user.getAddress().toDto() : null)
                .build();
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal == null) return null;
        return findByEmail(principal instanceof UserDetails userDetails
                ? userDetails.getUsername()
                : principal.toString());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserDto modifyUser(ModifyUserDto modifyUserDto) {
        User user = getCurrentUser();
        if(modifyUserDto.getFirstName() != null) {
            user.setFirstName(modifyUserDto.getFirstName());
        }
        if(modifyUserDto.getLastName() != null) {
            user.setLastName(modifyUserDto.getLastName());
        }
        AddressDto addressDto = modifyUserDto.getAddressDto();
        if(addressDto != null) {
            Address address = user.getAddress();
            address.setStreet(addressDto.getStreet());
            address.setPhone(addressDto.getPhoneNumber());
            if(addressDto.getLatitude() != null) {
                address.setLatitude(addressDto.getLatitude());
            }
            if (addressDto.getLongitude() != null) {
                address.setLongitude(addressDto.getLongitude());
            }
            Country country = address.getCity().getCountry();
            if(!Objects.equals(addressDto.getCountry(), address.getCity().getCountry().getName())) {
                country = getCountryOrAddNew(addressDto.getCountry());
            }
            if(!Objects.equals(addressDto.getCity(), address.getCity().getName())) {
                address.setCity(getCityOrAddNew(addressDto.getCity(), country));
            }
            user.setAddress(address);
        }
        User savedUser = userRepository.save(user);
        return loadUserByUsername(savedUser.getEmail());
    }

    @Override
    public ConfirmationToken findByConfirmationToken(String confirmationToken) {
        return confirmationTokenRepository.findByToken(confirmationToken);
    }

    @Override
    public void confirmEmail(String email) {
        User user = findByEmail(email);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public ConfirmationTokenDto getTokenToResetPassword(User user) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(user);
        if(confirmationToken == null) {
            confirmationToken = new ConfirmationToken(user);
        }
        confirmationToken.setToken(UUID.randomUUID().toString());
        confirmationToken.setCreatedDate(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken.toDto();
    }

    @Override
    public ConfirmationTokenDto getTokenToConfirmEmail(User user) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(user);
        if(confirmationToken == null) {
            confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);
        }
        return confirmationToken.toDto();
    }

    private static Set<GrantedAuthority> getAuthorities (Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

}
