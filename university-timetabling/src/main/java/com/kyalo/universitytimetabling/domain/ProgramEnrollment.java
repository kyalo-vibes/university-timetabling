package com.kyalo.universitytimetabling.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "program_enrollments")
public class ProgramEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    @JsonBackReference("program-programEnrollment")
    private Program program;

    @Column(name = "enrollment_year", nullable = false)
    private int year;

    @Column(name = "enrollment_number", nullable = false)
    private int enrolledNumber;

    public ProgramEnrollment() {
    }

    public ProgramEnrollment(Program program, int year, int enrolledNumber) {
        this.program = program;
        this.year = year;
        this.enrolledNumber = enrolledNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getEnrolledNumber() {
        return enrolledNumber;
    }

    public void setEnrolledNumber(int enrolledNumber) {
        this.enrolledNumber = enrolledNumber;
    }
}
