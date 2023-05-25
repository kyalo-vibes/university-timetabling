package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Program;
import com.kyalo.universitytimetabling.domain.ScheduleResult;
import com.kyalo.universitytimetabling.service.ProgramService;
import com.kyalo.universitytimetabling.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ProgramService programService;

    public ScheduleController(ScheduleService scheduleService, ProgramService programService) {
        this.scheduleService = scheduleService;
        this.programService = programService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Void> generateSchedule(@RequestParam int semester) {
        try {
            scheduleService.generateSchedule(semester);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Log the error message
            // e.g., logger.error("Failed to generate schedule: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/timetables")
    public ResponseEntity<List<ScheduleResult>> getGeneratedSchedules() {
        try {
            List<ScheduleResult> scheduleResults = scheduleService.getAllScheduleResults();
            return new ResponseEntity<>(scheduleResults, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error message
            // e.g., logger.error("Failed to get schedules: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{programId}/{year}/{semester}")
    public Map<String, ScheduleResult> getSchedulesForProgramIdYearAndSemester(
            @PathVariable Long programId,
            @PathVariable int year,
            @PathVariable int semester) {
        return scheduleService.getSchedulesForProgramIdYearAndSemester(programId, year, semester);
    }

    @GetMapping("/schedules/instructor/{instructorId}")
    public ResponseEntity<Map<String, ScheduleResult>> getSchedulesForInstructorId(@PathVariable Long instructorId) {
        Map<String, ScheduleResult> scheduleResults = scheduleService.getSchedulesForInstructorId(instructorId);
        if(scheduleResults.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scheduleResults, HttpStatus.OK);
    }

}
