package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.*;
import com.kyalo.universitytimetabling.repository.DepartmentRepository;
import com.kyalo.universitytimetabling.repository.InstructorRepository;
import com.kyalo.universitytimetabling.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final DepartmentRepository departmentRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, DepartmentRepository departmentRepository, TimeSlotRepository timeSlotRepository) {
        this.instructorRepository = instructorRepository;
        this.departmentRepository = departmentRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<InstructorDTO> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(instructor -> {
                    InstructorDTO dto = new InstructorDTO();
                    dto.setId(instructor.getId());
                    dto.setFirstName(instructor.getFirstName());
                    dto.setLastName(instructor.getLastName());
                    dto.setDeptName(instructor.getDepartment().getName()); // Assuming that the department is another entity with its own name
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public Optional<Instructor> getInstructorById(Long id) {
        return instructorRepository.findById(id);
    }

    public Instructor createInstructor(InstructorDTO instructorDto) {
        System.out.println(instructorDto.toString());

        Instructor instructor = new Instructor();
        instructor.setFirstName(instructorDto.getFirstName());
        instructor.setLastName(instructorDto.getLastName());

        Department department = departmentRepository.findByName(instructorDto.getDeptName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid department name:" + instructorDto.getDeptName()));
        instructor.setDepartment(department);

        return instructorRepository.save(instructor);
    }
    public InstructorDTO updateInstructor(Long id, InstructorDTO instructorDto) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(id);
        if (instructorOptional.isPresent()) {
            Instructor instructor = instructorOptional.get();
            instructor.setFirstName(instructorDto.getFirstName());
            instructor.setLastName(instructorDto.getLastName());

            // Look up the Department by its name.
            Department department = departmentRepository.findByName(instructorDto.getDeptName())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with name: " + instructorDto.getDeptName()));

            // Set the Department on the Instructor.
            instructor.setDepartment(department);

            Instructor updatedInstructor = instructorRepository.save(instructor);
            return new InstructorDTO(updatedInstructor.getFirstName(), updatedInstructor.getLastName(), updatedInstructor.getDepartment().getName());
        } else {
            throw new EntityNotFoundException("Instructor not found with id: " + id);
        }
    }






    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }

    public Instructor addPreference(Long instructorId, Long timeslotId) {
        // Fetch the instructor by id
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found with id: " + instructorId));

        // Fetch the timeslot by id
        TimeSlot timeSlot = timeSlotRepository.findById(timeslotId)
                .orElseThrow(() -> new EntityNotFoundException("Timeslot not found with id: " + timeslotId));

        // Add the timeslot to the instructor's preferences
        instructor.getPreferences().add(timeSlot);

        // Save the instructor and return
        return instructorRepository.save(instructor);
    }

    public InstructorDTO updatePreference(Long instructorId, PreferenceDto preferenceDto) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found with id: " + instructorId));

        TimeSlot timeSlot = timeSlotRepository.findByDayAndStartTime(preferenceDto.getDay(), preferenceDto.getStartTime())
                .orElseThrow(() -> new EntityNotFoundException("Timeslot not found for day: " + preferenceDto.getDay() + ", startTime: " + preferenceDto.getStartTime()));

        // Remove the existing timeslot for the specified day and start time if exists
        instructor.getPreferences().removeIf(slot -> slot.getDay().equals(preferenceDto.getDay()) && slot.getStartTime().equals(preferenceDto.getStartTime()));

        // Add the new timeslot to the instructor's preferences
        instructor.getPreferences().add(timeSlot);

        Instructor updatedInstructor = instructorRepository.save(instructor);

        // convert to DTO and return
        InstructorDTO instructorDTO = new InstructorDTO();
        // set DTO fields based on updatedInstructor
        return instructorDTO;
    }


    public List<InstructorPreferencesDto> getAllInstructorPreferences() {
        return instructorRepository.findAll().stream()
                .filter(instructor -> !instructor.getPreferences().isEmpty())
                .map(instructor -> {
                    InstructorPreferencesDto dto = new InstructorPreferencesDto();
                    dto.setInstructorName(instructor.getFirstName() + " " + instructor.getLastName());
                    dto.setPreferences(instructor.getPreferences().stream().map(timeSlot -> {
                        PreferenceDto preferenceDto = new PreferenceDto(timeSlot.getDay(), timeSlot.getStartTime());
                        preferenceDto.setId(timeSlot.getId());  // Assuming TimeSlot has an 'id' property.
                        return preferenceDto;
                    }).collect(Collectors.toSet()));
                    return dto;
                }).collect(Collectors.toList());
    }


}
