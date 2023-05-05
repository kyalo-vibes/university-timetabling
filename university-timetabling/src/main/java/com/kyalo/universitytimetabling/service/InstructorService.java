package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Instructor;
import com.kyalo.universitytimetabling.repository.InstructorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public Optional<Instructor> getInstructorById(Long id) {
        return instructorRepository.findById(id);
    }

    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public Instructor updateInstructor(Instructor instructor) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(instructor.getId());
        if (instructorOptional.isPresent()) {
            return instructorRepository.save(instructor);
        } else {
            throw new EntityNotFoundException("Instructor not found with id: " + instructor.getId());
        }
    }


    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }
}
