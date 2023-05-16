package com.kyalo.universitytimetabling.domain;

import java.util.List;

public class ScheduleResult {
    private List<Schedule> schedules;
    private String message;

    public ScheduleResult() {
    }

    public ScheduleResult(List<Schedule> schedules, String message) {
        this.schedules = schedules;
        this.message = message;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
