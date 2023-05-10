package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Course;
import com.kyalo.universitytimetabling.domain.Section;
import com.kyalo.universitytimetabling.repository.CourseRepository;
import com.kyalo.universitytimetabling.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, SectionRepository sectionRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> updateCourse(Long id, Course updatedCourse) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setCourseCode(updatedCourse.getCourseCode());
            course.setCourseName(updatedCourse.getCourseName());
            course.setDepartment(updatedCourse.getDepartment());
            course.setInstructor(updatedCourse.getInstructor());
            return Optional.of(courseRepository.save(course));
        }
        return Optional.empty();
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    // Section-related methods
    public List<Section> getAllSectionsByCourseId(Long courseId) {
        return sectionRepository.findByCourseId(courseId);
    }

    public Optional<Section> getSectionById(Long id) {
        return sectionRepository.findById(id);
    }

    public Section createSection(Long courseId, Section section) {
        return courseRepository.findById(courseId).map(course -> {
            section.setCourse(course);
            return sectionRepository.save(section);
        }).orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    public Optional<Section> updateSection(Long id, Section updatedSection) {
        Optional<Section> sectionOptional = sectionRepository.findById(id);
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            section.setNumberOfClasses(updatedSection.getNumberOfClasses());
            return Optional.of(sectionRepository.save(section));
        }
        return Optional.empty();
    }

    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }
}
