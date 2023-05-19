package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.*;
import com.kyalo.universitytimetabling.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final ProgramRepository programRepository;
    private final DepartmentRepository departmentRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, SectionRepository sectionRepository, ProgramRepository programRepository, DepartmentRepository departmentRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.programRepository = programRepository;
        this.departmentRepository = departmentRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CourseDTO convertToDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseCode(course.getCourseCode());
        courseDTO.setCourseName(course.getCourseName());
        courseDTO.setYear(course.getYear());
        courseDTO.setSemester(course.getSemester());
        courseDTO.setProgrammeName(course.getProgram().getName());
        courseDTO.setDeptName(course.getDepartment().getName());
        courseDTO.setInstructorName(course.getInstructor().getFirstName() + " " + course.getInstructor().getLastName());
        return courseDTO;
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(CourseDTO courseDto) {
        System.out.println(courseDto.toString());

        Course course = new Course();
        course.setCourseCode(courseDto.getCourseCode());
        course.setCourseName(courseDto.getCourseName());
        course.setYear(courseDto.getYear());
        course.setSemester(courseDto.getSemester());

        Program program = programRepository.findByName(courseDto.getProgrammeName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid programme name:" + courseDto.getProgrammeName()));
        course.setProgram(program);

        Department department = departmentRepository.findByName(courseDto.getDeptName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid department name:" + courseDto.getDeptName()));
        course.setDepartment(department);

        Instructor instructor = instructorRepository.findByFirstName(courseDto.getInstructorName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid instructor name:" + courseDto.getInstructorName()));
        course.setInstructor(instructor);

        return courseRepository.save(course);
    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setCourseCode(courseDTO.getCourseCode());
            course.setCourseName(courseDTO.getCourseName());
            course.setYear(courseDTO.getYear());
            course.setSemester(courseDTO.getSemester());

            // Here you need to get the Department and Instructor based on their name and set to the course
            Department department = departmentRepository.findByName(courseDTO.getDeptName())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with name: " + courseDTO.getDeptName()));
            course.setDepartment(department);

            Instructor instructor = instructorRepository.findByFirstName(courseDTO.getInstructorName())
                    .orElseThrow(() -> new EntityNotFoundException("Instructor not found with name: " + courseDTO.getInstructorName()));
            course.setInstructor(instructor);

            // Same for the Programme
            Program programme = programRepository.findByName(courseDTO.getProgrammeName())
                    .orElseThrow(() -> new EntityNotFoundException("Programme not found with name: " + courseDTO.getProgrammeName()));
            course.setProgram(programme);

            Course updatedCourse = courseRepository.save(course);

            // After saving, map the course back to the DTO
            CourseDTO updatedCourseDTO = new CourseDTO(updatedCourse.getCourseCode(), updatedCourse.getCourseName(),
                    updatedCourse.getYear(), updatedCourse.getSemester(), updatedCourse.getProgram().getName(),
                    updatedCourse.getDepartment().getName(), updatedCourse.getInstructor().getFirstName());

            return updatedCourseDTO;
        } else {
            throw new EntityNotFoundException("Course not found with id: " + id);
        }
    }


    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
        Optional<Course> course = courseRepository.findById(id);
        if(course != null) {
            System.out.println("Course still exists");
        }
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
