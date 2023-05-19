package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Faculty;
import com.kyalo.universitytimetabling.domain.FacultyDTO;
import com.kyalo.universitytimetabling.repository.FacultyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public List<FacultyDTO> getAllFaculties() {
        return facultyRepository.findAll().stream()
                .map(faculty -> new FacultyDTO(faculty.getFacultyCode(), faculty.getFacultyName()))
                .collect(Collectors.toList());
    }
    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }

    public Faculty createFaculty(FacultyDTO facultyDTO) {
        Faculty faculty = new Faculty();
        faculty.setFacultyCode(facultyDTO.getFacultyCode());
        faculty.setFacultyName(facultyDTO.getFacultyName());
        return facultyRepository.save(faculty);
    }
    public FacultyDTO updateFaculty(Long id, FacultyDTO facultyDTO) {
        Optional<Faculty> facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isPresent()) {
            Faculty faculty = facultyOptional.get();
            faculty.setFacultyCode(facultyDTO.getFacultyCode());
            faculty.setFacultyName(facultyDTO.getFacultyName());
            Faculty updatedFaculty = facultyRepository.save(faculty);
            return new FacultyDTO(updatedFaculty.getFacultyCode(), updatedFaculty.getFacultyName());
        } else {
            throw new EntityNotFoundException("Faculty not found with id: " + id);
        }
    }


    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }
}
