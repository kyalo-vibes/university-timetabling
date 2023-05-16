package com.kyalo.universitytimetabling.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(name = "dept_id", nullable = false)
    @JsonBackReference
    private Department department;

    @ManyToMany
    private List<TimeSlot> occupiedTimeSlots = new ArrayList<>();

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
}
