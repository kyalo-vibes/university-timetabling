package com.kyalo.universitytimetabling.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id;


    @Column(name = "dept_name")
    private String name;

    @Column(name = "dept_code")
    private String dept_code;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    @JsonBackReference("faculty-department")
    private Faculty faculty;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("department-course")
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference("room-department")
    private List<Room> rooms = new ArrayList<>();

    @ManyToMany(mappedBy = "departments")
    private List<Room> roomsByDept = new ArrayList<>();

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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRoomsByDept() {
        return roomsByDept;
    }

    public void setRoomsByDept(List<Room> roomsByDept) {
        this.rooms = rooms;
    }

    public void addRoom(Room room) {
        this.roomsByDept.add(room);
    }

    public void removeRoom(Room room) {
        this.roomsByDept.remove(room);
    }
}
