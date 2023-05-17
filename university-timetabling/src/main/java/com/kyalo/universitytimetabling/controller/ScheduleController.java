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
    public ResponseEntity<Map<String, List<ScheduleResult>>> generateSchedule(@RequestParam int semester) {
        try {
            Map<String, List<ScheduleResult>> scheduleResults = scheduleService.generateSchedule(semester);

            return ResponseEntity.ok(scheduleResults);
        } catch (Exception e) {
            // We cannot return a map in the case of a bad request, so we will return an empty ResponseEntity
            return ResponseEntity.badRequest().build();
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
