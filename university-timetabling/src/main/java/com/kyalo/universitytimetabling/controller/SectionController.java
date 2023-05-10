package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Section;
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
    public ResponseEntity<List<Section>> findAll() {
        List<Section> sections = sectionService.findAll();
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
    public ResponseEntity<Section> save(@RequestBody Section section) {
        Section savedSection = sectionService.save(section);
        return new ResponseEntity<>(savedSection, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Section> update(@PathVariable Long id, @RequestBody Section updatedSection) {
        Section existingSection = sectionService.findById(id);
        if (existingSection != null) {
            updatedSection.setId(id);
            Section savedSection = sectionService.save(updatedSection);
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
