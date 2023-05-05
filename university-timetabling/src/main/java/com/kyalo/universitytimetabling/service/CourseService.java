package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Course;
import com.kyalo.universitytimetabling.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
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
}
