package com.kyalo.universitytimetabling.domain;

public class InstructorDTO {
    private String firstName;
    private String lastName;
    private String deptName;

    public InstructorDTO() {
    }

    public InstructorDTO(String firstName, String lastName, String deptName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.deptName = deptName;
    }

    // getters and setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "InstructorDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", deptName=" + deptName +
                '}';
    }

}
