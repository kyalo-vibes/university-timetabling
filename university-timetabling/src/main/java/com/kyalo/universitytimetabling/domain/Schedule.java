package com.kyalo.universitytimetabling.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Course course;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "time_slot_id")
    @JsonBackReference
    private TimeSlot timeSlot;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "section_id")
    private Section section;

    // Default constructor
    public Schedule() {
    }

    public Schedule(Course course, Room room, TimeSlot timeSlot) {
        this.course = course;
        this.room = room;
        this.timeSlot = timeSlot;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
