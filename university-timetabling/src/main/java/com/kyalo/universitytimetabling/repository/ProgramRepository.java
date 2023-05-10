package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
}
