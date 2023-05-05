package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
