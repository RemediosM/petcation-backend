package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
