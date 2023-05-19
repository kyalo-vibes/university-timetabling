package com.kyalo.universitytimetabling.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kyalo.universitytimetabling.domain.converter.LocalTimeAttributeConverter;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "time_slots")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day")
    private String day;

    @Column(name = "start_time")
    @Convert(converter = LocalTimeAttributeConverter.class)
    private LocalTime startTime;

    @Column(name = "end_time")
    @Convert(converter = LocalTimeAttributeConverter.class)
    private LocalTime endTime;

    public TimeSlot() {
    }

    public TimeSlot(String day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(dtf) + " - " + endTime.format(dtf);
    }

    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("timeSlot-schedule")
    private List<Schedule> schedules = new ArrayList<>();

    public List<String> getAllTimes() {
        return schedules.stream()
                .map(schedule -> schedule.getTimeSlot().getDay() + ": " + getTime())
                .collect(Collectors.toList());
    }
}
