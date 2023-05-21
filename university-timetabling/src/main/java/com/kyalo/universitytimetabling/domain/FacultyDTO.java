package com.kyalo.universitytimetabling.domain;

public class FacultyDTO {
    private Long id;
    private String facultyCode;
    private String facultyName;

    public FacultyDTO() {
    }

    public FacultyDTO(Long id, String facultyCode, String facultyName) {
        this.id = id;
        this.facultyCode = facultyCode;
        this.facultyName = facultyName;
    }

    // getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

