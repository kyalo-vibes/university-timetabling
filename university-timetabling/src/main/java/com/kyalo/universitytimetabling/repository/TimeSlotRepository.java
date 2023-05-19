package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    Optional<TimeSlot> findByDayAndStartTime(String day, LocalTime startTime);
}
