package com.kyalo.universitytimetabling.repository;

import com.kyalo.universitytimetabling.domain.Course;
import com.kyalo.universitytimetabling.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findBySemester(int semester);
    List<Course> findBySemesterAndYearAndProgram(int semester, int year, Program program);


}
