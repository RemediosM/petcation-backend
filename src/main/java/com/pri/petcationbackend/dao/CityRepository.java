package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.City;
import com.pri.petcationbackend.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByNameAndCountry(String name, Country country);
}
