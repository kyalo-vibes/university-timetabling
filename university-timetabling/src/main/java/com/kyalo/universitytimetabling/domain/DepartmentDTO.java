package com.kyalo.universitytimetabling.domain;

public class DepartmentDTO {
    private String deptCode;
    private String deptName;
    private String facultyName;

    public DepartmentDTO() {
    }

    public DepartmentDTO(String deptCode, String deptName, String facultyName) {
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.facultyName = facultyName;
    }

    // getters and setters

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "deptCode='" + deptCode + '\'' +
                ", deptName='" + deptName + '\'' +
                ", facultyName=" + facultyName +
                '}';
    }

}

