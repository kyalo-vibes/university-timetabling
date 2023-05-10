package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Program;
import com.kyalo.universitytimetabling.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
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
}
