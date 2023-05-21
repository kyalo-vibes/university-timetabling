package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Faculty;
import com.kyalo.universitytimetabling.domain.Program;
import com.kyalo.universitytimetabling.domain.ProgrammeDTO;
import com.kyalo.universitytimetabling.repository.FacultyRepository;
import com.kyalo.universitytimetabling.repository.ProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository, FacultyRepository facultyRepository) {
        this.programRepository = programRepository;
        this.facultyRepository = facultyRepository;
    }

    public Program createProgramme(ProgrammeDTO programmeDto) {
        // Print the ProgrammeDTO object
        System.out.println(programmeDto.toString());

        Program programme = new Program();
        programme.setCode(programmeDto.getProgrammeCode());
        programme.setName(programmeDto.getProgrammeName());

        Faculty faculty = facultyRepository.findByFacultyName(programmeDto.getFacultyName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid faculty name:" + programmeDto.getFacultyName()));
        programme.setFaculty(faculty);

        return programRepository.save(programme);
    }

    public List<ProgrammeDTO> getAllPrograms() {
        List<Program> programs = programRepository.findAll();
        List<ProgrammeDTO> programDTOs = new ArrayList<>();
        for (Program program : programs) {
            ProgrammeDTO programDTO = new ProgrammeDTO();
            programDTO.setId(program.getId());
            programDTO.setProgrammeCode(program.getCode());
            programDTO.setProgrammeName(program.getName());
            programDTO.setFacultyName(program.getFaculty().getFacultyName()); // assuming there is a getName() method in Faculty class
            programDTOs.add(programDTO);
        }
        return programDTOs;
    }


    public Program saveProgram(Program program) {
        return programRepository.save(program);
    }

    public Program getProgramById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found with id: " + id));
    }

    public void deleteProgramById(Long id) {
        programRepository.deleteById(id);
    }

    public ProgrammeDTO updateProgram(Long id, ProgrammeDTO programmeDTO) {
        Optional<Program> programOptional = programRepository.findById(id);
        if (programOptional.isPresent()) {
            Program program = programOptional.get();
            program.setCode(programmeDTO.getProgrammeCode());
            program.setName(programmeDTO.getProgrammeName());
            Faculty faculty = facultyRepository.findByFacultyName(programmeDTO.getFacultyName())
                    .orElseThrow(() -> new EntityNotFoundException("Faculty not found with name: " + programmeDTO.getFacultyName()));
            program.setFaculty(faculty);
            Program updatedProgram = programRepository.save(program);

            return new ProgrammeDTO(updatedProgram.getCode(), updatedProgram.getName(), updatedProgram.getFaculty().getFacultyName());
        } else {
            throw new EntityNotFoundException("Program not found with id: " + id);
        }
    }

}
