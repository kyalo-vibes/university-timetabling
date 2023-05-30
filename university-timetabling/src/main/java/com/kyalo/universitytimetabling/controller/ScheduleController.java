package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Program;
import com.kyalo.universitytimetabling.domain.ScheduleResult;
import com.kyalo.universitytimetabling.domain.ScheduleStatus;
import com.kyalo.universitytimetabling.repository.ScheduleStatusRepository;
import com.kyalo.universitytimetabling.service.ProgramService;
import com.kyalo.universitytimetabling.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ProgramService programService;
    private final ScheduleStatusRepository scheduleStatusRepository;

    public ScheduleController(ScheduleService scheduleService, ProgramService programService, ScheduleStatusRepository scheduleStatusRepository) {
        this.scheduleService = scheduleService;
        this.programService = programService;
        this.scheduleStatusRepository = scheduleStatusRepository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/generate")
    public ResponseEntity<Void> generateSchedule(@RequestParam int semester) {
        try {
            scheduleService.generateSchedule(semester);
            return new ResponseEntity<>(HttpStatus.ACCEPTED); // Return 202 (Accepted) status
        } catch (Exception e) {
            // Log the error message
            // e.g., logger.error("Failed to generate schedule: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<String> getScheduleGenerationStatus(@RequestParam int semester) {
        Optional<ScheduleStatus> scheduleStatus = scheduleStatusRepository.findBySemester(semester);
        if (scheduleStatus.isPresent()) {
            return new ResponseEntity<>(scheduleStatus.get().getStatus(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("PENDING", HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/myTimetable")
    public Map<String, ScheduleResult> getSchedulesForLoggedInUser(Principal principal) {
        return scheduleService.getSchedulesForLoggedInUser(principal.getName());
    }


    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    @GetMapping("/instructor")
    public ResponseEntity<Map<String, ScheduleResult>> getSchedulesForInstructor(Principal principal) {
        Map<String, ScheduleResult> scheduleResults = scheduleService.getSchedulesForInstructor(principal.getName());
        if(scheduleResults.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scheduleResults, HttpStatus.OK);
    }


}
