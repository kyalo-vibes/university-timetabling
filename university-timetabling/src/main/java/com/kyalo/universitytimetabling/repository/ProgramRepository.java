package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.Faculty;
import com.kyalo.universitytimetabling.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    Optional<Program> findByName(String name);

}
