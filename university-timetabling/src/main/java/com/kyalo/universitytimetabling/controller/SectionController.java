package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Section;
import com.kyalo.universitytimetabling.domain.SectionDTO;
import com.kyalo.universitytimetabling.service.SectionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@PreAuthorize("hasAuthority('ADMIN')")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping
    public ResponseEntity<List<SectionDTO>> getAllSections() {
        List<SectionDTO> sections = sectionService.getAllSections();
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Section> getSectionById(@PathVariable Long id) {
        Section section = sectionService.getSectionById(id);
        if (section != null) {
            return new ResponseEntity<>(section, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody SectionDTO sectionDto) {
        Section savedSection = sectionService.createSection(sectionDto);
        return new ResponseEntity<>(savedSection, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionDTO> updateSection(@PathVariable Long id, @RequestBody SectionDTO sectionDTO) {
        try {
            SectionDTO updatedSection = sectionService.updateSection(id, sectionDTO);
            return new ResponseEntity<>(updatedSection, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        try {
            sectionService.deleteSection(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
