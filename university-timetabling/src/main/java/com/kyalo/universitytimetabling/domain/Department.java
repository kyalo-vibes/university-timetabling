package com.kyalo.universitytimetabling.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "dept_name")
    private String name;

    @Column(name = "dept_code")
    private String dept_code;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Department() {
    }

    public Department(String name, String dept_code, Faculty faculty) {
        this.name = name;
        this.dept_code = dept_code;
        this.faculty = faculty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept_code() {
        return dept_code;
    }

    public void setDept_code(String dept_code) {
        this.dept_code = dept_code;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }
}
