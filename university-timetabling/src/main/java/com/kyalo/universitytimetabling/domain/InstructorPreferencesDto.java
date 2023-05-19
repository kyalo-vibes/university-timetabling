package com.kyalo.universitytimetabling.domain;

import java.time.LocalTime;
import java.util.Set;

public class InstructorPreferencesDto {
    private String instructorName;
    private Set<PreferenceDto> preferences;

    public InstructorPreferencesDto() {
    }

    public InstructorPreferencesDto(String instructorName, Set<PreferenceDto> preferences) {
        this.instructorName = instructorName;
        this.preferences = preferences;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Set<PreferenceDto> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<PreferenceDto> preferences) {
        this.preferences = preferences;
    }
}
