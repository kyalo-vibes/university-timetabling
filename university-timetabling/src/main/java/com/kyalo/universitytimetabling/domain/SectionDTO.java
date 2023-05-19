package com.kyalo.universitytimetabling.domain;

public class SectionDTO {
    private int numberOfClasses;
    private String courseName;

    public SectionDTO() {
    }

    public SectionDTO(int numberOfClasses, String courseName) {
        this.numberOfClasses = numberOfClasses;
        this.courseName = courseName;
    }

    // getters and setters

    public int getNumberOfClasses() {
        return numberOfClasses;
    }

    public void setNumberOfClasses(int numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "SectionDTO{" +
                "numberOfClasses=" + numberOfClasses +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
