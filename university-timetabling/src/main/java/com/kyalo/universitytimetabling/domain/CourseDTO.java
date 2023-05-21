package com.kyalo.universitytimetabling.domain;

public class CourseDTO {
    private Long id;
    private String courseCode;
    private String courseName;
    private int year;
    private int semester;
    private String programmeName;
    private String deptName;
    private String instructorName;

    public CourseDTO() {
    }

    public CourseDTO(String courseCode, String courseName, int year, int semester, String programmeName, String deptName, String instructorName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.year = year;
        this.semester = semester;
        this.programmeName = programmeName;
        this.deptName = deptName;
        this.instructorName = instructorName;
    }

    // getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", year='" + year + '\'' +
                ", semester='" + semester + '\'' +
                ", programmeName='" + programmeName + '\'' +
                ", deptName='" + deptName + '\'' +
                ", instructorName='" + instructorName + '\'' +
                '}';
    }
}
