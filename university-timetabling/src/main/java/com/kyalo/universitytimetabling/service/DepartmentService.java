package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Department;
import com.kyalo.universitytimetabling.domain.DepartmentDTO;
import com.kyalo.universitytimetabling.domain.Faculty;
import com.kyalo.universitytimetabling.repository.DepartmentRepository;
import com.kyalo.universitytimetabling.repository.FacultyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, FacultyRepository facultyRepository) {
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Department createDepartment(DepartmentDTO departmentDto) {
        // Print the DepartmentDTO object
        System.out.println(departmentDto.toString());
        Department department = new Department();
        department.setDept_code(departmentDto.getDeptCode());
        department.setName(departmentDto.getDeptName());

        Faculty faculty = facultyRepository.findByFacultyName(departmentDto.getFacultyName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid faculty name:" + departmentDto.getFacultyName()));
        department.setFaculty(faculty);

        return departmentRepository.save(department);
    }





    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            department.setDept_code(departmentDTO.getDeptCode());
            department.setName(departmentDTO.getDeptName());

            // Fetching related Faculty
            Optional<Faculty> faculty = facultyRepository.findByFacultyName(departmentDTO.getFacultyName());
            if (faculty == null) {
                throw new EntityNotFoundException("Faculty not found with name: " + departmentDTO.getFacultyName());
            }
            department.setFaculty(faculty.get());

            Department updatedDepartment = departmentRepository.save(department);
            return new DepartmentDTO(
                    updatedDepartment.getDept_code(),
                    updatedDepartment.getName(),
                    updatedDepartment.getFaculty().getFacultyName());
        } else {
            throw new EntityNotFoundException("Department not found with id: " + id);
        }
    }
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> departmentDTOs = new ArrayList<>();

        for (Department department : departments) {
            Faculty faculty = department.getFaculty();
            DepartmentDTO departmentDTO = new DepartmentDTO(
                    department.getDept_code(),
                    department.getName(),
                    faculty != null ? faculty.getFacultyName() : null);
            departmentDTOs.add(departmentDTO);
        }

        return departmentDTOs;
    }}
