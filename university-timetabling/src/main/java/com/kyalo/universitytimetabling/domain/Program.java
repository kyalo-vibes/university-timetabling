package com.kyalo.universitytimetabling.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "programmes")
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "programme_id")
    private Long id;

    @Column(name = "programme_code")
    private String code;

    @Column(name = "programme_name")
    private String name;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<Course> courses;

    public Program() {
    }

    public Program(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
