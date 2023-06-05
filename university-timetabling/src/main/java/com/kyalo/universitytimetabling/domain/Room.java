package com.kyalo.universitytimetabling.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_capacity")
    private int capacity;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "is_available")
    private boolean isAvailable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id", nullable = false)
    @JsonBackReference("room-department")
    private Department department;

    @ManyToMany
    private List<TimeSlot> occupiedTimeSlots = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "room_department",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "dept_id"))
    private List<Department> departments = new ArrayList<>();

    public Room() {
    }

    public Room(String roomName, int capacity, String roomType, boolean isAvailable) {
        this.roomName = roomName;
        this.capacity = capacity;
        this.roomType = roomType;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isAvailable(TimeSlot timeSlot) {
        return !occupiedTimeSlots.contains(timeSlot);
    }

    public void occupyTimeSlot(TimeSlot timeSlot) {
        occupiedTimeSlots.add(timeSlot);
    }

    public void freeTimeSlot(TimeSlot timeSlot) {
        occupiedTimeSlots.remove(timeSlot);
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public void addDepartment(Department department) {
        this.departments.add(department);
    }

    public void removeDepartment(Department department) {
        this.departments.remove(department);
    }
}
