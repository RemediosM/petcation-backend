package com.pri.petcationbackend.dao;

import com.pri.petcationbackend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
