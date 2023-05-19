package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Program;
import com.kyalo.universitytimetabling.domain.ProgrammeDTO;
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
    public ResponseEntity<List<ProgrammeDTO>> getAllPrograms() {
        List<ProgrammeDTO> programDTOs = programService.getAllPrograms();
        return new ResponseEntity<>(programDTOs, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Program> createProgram(@RequestBody ProgrammeDTO programmeDto) {
        Program savedProgram = programService.createProgramme(programmeDto);
        return new ResponseEntity<>(savedProgram, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgramById(@PathVariable("id") Long id) {
        Program program = programService.getProgramById(id);
        return new ResponseEntity<>(program, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgrammeDTO> updateProgram(@PathVariable("id") Long id, @RequestBody ProgrammeDTO programmeDTO) {
        ProgrammeDTO updatedProgramDTO = programService.updateProgram(id, programmeDTO);
        return new ResponseEntity<>(updatedProgramDTO, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable("id") Long id) {
        programService.deleteProgramById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
