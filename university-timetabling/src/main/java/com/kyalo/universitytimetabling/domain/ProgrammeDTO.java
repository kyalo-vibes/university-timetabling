package com.kyalo.universitytimetabling.domain;

public class ProgrammeDTO {
    private String programmeCode;
    private String programmeName;
    private String facultyName;

    public ProgrammeDTO() {
    }

    public ProgrammeDTO(String programmeCode, String programmeName, String facultyName) {
        this.programmeCode = programmeCode;
        this.programmeName = programmeName;
        this.facultyName = facultyName;
    }

    // getters and setters

    public String getProgrammeCode() {
        return programmeCode;
    }

    public void setProgrammeCode(String programmeCode) {
        this.programmeCode = programmeCode;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    @Override
    public String toString() {
        return "ProgrammeDTO{" +
                "programmeCode='" + programmeCode + '\'' +
                ", programmeName='" + programmeName + '\'' +
                ", facultyName='" + facultyName + '\'' +
                '}';
    }
}
