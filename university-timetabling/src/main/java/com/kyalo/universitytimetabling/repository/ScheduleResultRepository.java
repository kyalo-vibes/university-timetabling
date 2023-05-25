package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.ScheduleResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleResultRepository extends JpaRepository<ScheduleResult, Long> {
}
