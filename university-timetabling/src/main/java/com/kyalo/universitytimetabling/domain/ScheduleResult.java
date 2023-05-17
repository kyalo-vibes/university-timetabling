package com.kyalo.universitytimetabling.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleResult {

    private List<String> courseCodes;
    private List<String> timeSlots;
    private List<String> instructorNames;
    private List<String> roomNames;
    private String message;

    public ScheduleResult() {
    }

    public ScheduleResult(List<Schedule> schedules, String message) {
        this.message = message;
        this.courseCodes = schedules.stream().map(schedule -> schedule.getCourse().getCourseCode()).collect(Collectors.toList());
        this.timeSlots = schedules.stream().flatMap(schedule -> schedule.getTimeSlot().getAllTimes().stream()).collect(Collectors.toList());
        this.instructorNames = schedules.stream().map(schedule -> schedule.getCourse().getInstructor().getFirstName()).collect(Collectors.toList());
        this.roomNames = schedules.stream().map(schedule -> schedule.getRoom().getRoomName()).collect(Collectors.toList());
    }

    public List<String> getCourseCodes() {
        return courseCodes;
    }

    public void setCourseCodes(List<String> courseCodes) {
        this.courseCodes = courseCodes;
    }

    public List<String> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<String> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<String> getInstructorNames() {
        return instructorNames;
    }

    public void setInstructorNames(List<String> instructorNames) {
        this.instructorNames = instructorNames;
    }

    public List<String> getRoomNames() {
        return roomNames;
    }

    public void setRoomNames(List<String> roomNames) {
        this.roomNames = roomNames;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
