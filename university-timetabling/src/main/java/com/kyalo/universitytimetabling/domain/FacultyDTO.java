package com.kyalo.universitytimetabling.domain;

public class FacultyDTO {
    private String facultyCode;
    private String facultyName;

    public FacultyDTO() {
    }

    public FacultyDTO(String facultyCode, String facultyName) {
        this.facultyCode = facultyCode;
        this.facultyName = facultyName;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}

