package com.kyalo.universitytimetabling.domain;

import java.time.LocalTime;

public class PreferenceDto {
    private String day;
    private LocalTime startTime;

    public PreferenceDto() {
    }

    public PreferenceDto(String day, LocalTime startTime) {
        this.day = day;
        this.startTime = startTime;
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
}

