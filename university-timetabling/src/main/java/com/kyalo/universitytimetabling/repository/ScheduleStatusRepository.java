package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleStatusRepository extends JpaRepository<ScheduleStatus, Long> {
    Optional<ScheduleStatus> findBySemester(int semester);
}
