package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Program;
import com.kyalo.universitytimetabling.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping
    public ResponseEntity<List<Program>> getAllPrograms() {
        List<Program> programs = programService.getAllPrograms();
        return new ResponseEntity<>(programs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Program> createProgram(@RequestBody Program program) {
        Program savedProgram = programService.saveProgram(program);
        return new ResponseEntity<>(savedProgram, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgramById(@PathVariable("id") Long id) {
        Program program = programService.getProgramById(id);
        return new ResponseEntity<>(program, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Program> updateProgram(@PathVariable("id") Long id, @RequestBody Program programDetails) {
        Program existingProgram = programService.getProgramById(id);
        existingProgram.setName(programDetails.getName());
        existingProgram.setCourses(programDetails.getCourses());
        Program updatedProgram = programService.saveProgram(existingProgram);
        return new ResponseEntity<>(updatedProgram, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable("id") Long id) {
        programService.deleteProgramById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
