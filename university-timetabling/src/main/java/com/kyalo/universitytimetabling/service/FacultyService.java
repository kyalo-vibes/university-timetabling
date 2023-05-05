package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Faculty;
import com.kyalo.universitytimetabling.repository.FacultyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty updateFaculty(Faculty faculty) {
        Optional<Faculty> facultyOptional = facultyRepository.findById(faculty.getId());
        if (facultyOptional.isPresent()) {
            return facultyRepository.save(faculty);
        } else {
            throw new EntityNotFoundException("Faculty not found with id: " + faculty.getId());
        }
    }


    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }
}
