package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Section;
import com.kyalo.universitytimetabling.domain.SectionDTO;
import com.kyalo.universitytimetabling.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping
    public ResponseEntity<List<SectionDTO>> findAll() {
        List<SectionDTO> sections = sectionService.findAll();
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Section> findById(@PathVariable Long id) {
        Section section = sectionService.findById(id);
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
    public ResponseEntity<SectionDTO> update(@PathVariable Long id, @RequestBody SectionDTO sectionDTO) {
        SectionDTO existingSection = sectionService.updateById(id);
        if (existingSection != null) {
            SectionDTO savedSection = sectionService.save(id, sectionDTO);
            return new ResponseEntity<>(savedSection, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        Section existingSection = sectionService.findById(id);
        if (existingSection != null) {
            sectionService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
