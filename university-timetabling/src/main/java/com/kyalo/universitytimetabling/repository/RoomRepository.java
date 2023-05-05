package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
