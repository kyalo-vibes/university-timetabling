package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.Program;
import com.kyalo.universitytimetabling.domain.ProgramEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramEnrollmentRepository extends JpaRepository<ProgramEnrollment, Long> {
    ProgramEnrollment findByProgramAndYear(Program program, int year);
}
