package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.Department;
import com.kyalo.universitytimetabling.domain.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
}
