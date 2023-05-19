package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Instructor;
import com.kyalo.universitytimetabling.domain.InstructorDTO;
import com.kyalo.universitytimetabling.domain.InstructorPreferencesDto;
import com.kyalo.universitytimetabling.domain.PreferenceDto;
import com.kyalo.universitytimetabling.service.InstructorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public ResponseEntity<Instructor> createInstructor(@RequestBody InstructorDTO instructorDto) {
        Instructor savedInstructor = instructorService.createInstructor(instructorDto);
        return new ResponseEntity<>(savedInstructor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstructorDTO> updateInstructor(@PathVariable Long id, @RequestBody InstructorDTO instructorDto) {
        InstructorDTO updatedInstructorDto = instructorService.updateInstructor(id, instructorDto);
        return new ResponseEntity<>(updatedInstructorDto, HttpStatus.OK);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{instructorId}/preferences/{timeslotId}")
    public ResponseEntity<Instructor> addPreference(@PathVariable Long instructorId, @PathVariable Long timeslotId) {
        Instructor instructor = instructorService.addPreference(instructorId, timeslotId);
        return ResponseEntity.ok(instructor);
    }

    @PutMapping("/{instructorId}/preferences")
    public ResponseEntity<InstructorDTO> updatePreference(@PathVariable Long instructorId, @RequestBody PreferenceDto preferenceDto) {
        InstructorDTO instructorDto = instructorService.updatePreference(instructorId, preferenceDto);
        return ResponseEntity.ok(instructorDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Long id) {
        Instructor instructor = instructorService.getInstructorById(id).orElseThrow(() -> new EntityNotFoundException("Instructor not found with id: " + id));
        return new ResponseEntity<>(instructor, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        List<InstructorDTO> instructors = instructorService.getAllInstructors();
        return new ResponseEntity<>(instructors, HttpStatus.OK);
    }


    @GetMapping("/preferences")
    public List<InstructorPreferencesDto> getAllInstructorPreferences() {
        return instructorService.getAllInstructorPreferences();
    }
}
