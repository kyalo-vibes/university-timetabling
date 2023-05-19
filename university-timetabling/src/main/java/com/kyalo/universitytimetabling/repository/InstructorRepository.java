package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.Faculty;
import com.kyalo.universitytimetabling.domain.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByFirstName(String firstName);
    List<Instructor> findByFirstNameAndLastName(String firstName, String lastName);

}
