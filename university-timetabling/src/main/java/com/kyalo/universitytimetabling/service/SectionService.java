package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Course;
import com.kyalo.universitytimetabling.domain.Section;
import com.kyalo.universitytimetabling.domain.SectionDTO;
import com.kyalo.universitytimetabling.repository.CourseRepository;
import com.kyalo.universitytimetabling.repository.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<SectionDTO> findAll() {
        List<Section> sections = sectionRepository.findAll();

        // map each Section entity to a SectionDTO
        List<SectionDTO> sectionDTOs = sections.stream().map(section -> {
            return new SectionDTO(
                    section.getNumberOfClasses(),
                    section.getCourse().getCourseName());
        }).collect(Collectors.toList());

        return sectionDTOs;
    }

    public Section findById(Long id) {
        return sectionRepository.findById(id).orElse(null);
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

    public void deleteById(Long id) {
        sectionRepository.deleteById(id);
    }

    public Section save(Section section) {
        return sectionRepository.save(section);
    }

    public SectionDTO updateById(Long id) {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if (optionalSection.isPresent()) {
            Section section = optionalSection.get();
            return new SectionDTO(section.getNumberOfClasses(), section.getCourse().getCourseName());
        }
        return null;
    }

    public SectionDTO save(Long id, SectionDTO sectionDTO) {
        Optional<Course> optionalCourse = courseRepository.findByCourseName(sectionDTO.getCourseName());
        if (optionalCourse.isPresent()) {
            Section section = new Section();
            section.setId(id);
            section.setNumberOfClasses(sectionDTO.getNumberOfClasses());
            section.setCourse(optionalCourse.get());

            Section savedSection = sectionRepository.save(section);
            return new SectionDTO(savedSection.getNumberOfClasses(), savedSection.getCourse().getCourseName());
        }
        throw new EntityNotFoundException("Course not found with name: " + sectionDTO.getCourseName());
    }

}
