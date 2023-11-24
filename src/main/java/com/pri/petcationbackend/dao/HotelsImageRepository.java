package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Hotel;
import com.pri.petcationbackend.model.HotelsImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelsImageRepository extends JpaRepository<HotelsImage, Long> {

    List<HotelsImage> findAllByHotel(Hotel hotel);

}
