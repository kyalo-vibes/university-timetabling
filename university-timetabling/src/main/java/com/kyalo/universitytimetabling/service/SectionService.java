package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Course;
import com.kyalo.universitytimetabling.domain.Section;
import com.kyalo.universitytimetabling.domain.SectionDTO;
import com.kyalo.universitytimetabling.repository.CourseRepository;
import com.kyalo.universitytimetabling.repository.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository, CourseRepository courseRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
    }

    public Section createSection(SectionDTO sectionDto) {
        System.out.println(sectionDto.toString());

        Section section = new Section();
        section.setNumberOfClasses(sectionDto.getNumberOfClasses());

        Course course = courseRepository.findByCourseName(sectionDto.getCourseName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid course name: " + sectionDto.getCourseName()));
        section.setCourse(course);

        return sectionRepository.save(section);
    }

    public SectionDTO updateSection(Long id, SectionDTO sectionDTO) {
        Optional<Section> sectionOptional = sectionRepository.findById(id);
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            section.setNumberOfClasses(sectionDTO.getNumberOfClasses());

            Optional<Course> course = courseRepository.findByCourseName(sectionDTO.getCourseName());
            if (!course.isPresent()) {
                throw new EntityNotFoundException("Course not found with name: " + sectionDTO.getCourseName());
            }
            section.setCourse(course.get());

            Section updatedSection = sectionRepository.save(section);
            return new SectionDTO(
                    updatedSection.getId(),
                    updatedSection.getNumberOfClasses(),
                    updatedSection.getCourse().getCourseName());
        } else {
            throw new EntityNotFoundException("Section not found with id: " + id);
        }
    }

    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }

    public Section getSectionById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + id));
    }

    public List<SectionDTO> getAllSections() {
        List<Section> sections = sectionRepository.findAll();
        List<SectionDTO> sectionDTOs = new ArrayList<>();

        for (Section section : sections) {
            Course course = section.getCourse();
            SectionDTO sectionDTO = new SectionDTO(
                    section.getId(),
                    section.getNumberOfClasses(),
                    course != null ? course.getCourseName() : null);
            sectionDTOs.add(sectionDTO);
        }

        return sectionDTOs;
    }
}
