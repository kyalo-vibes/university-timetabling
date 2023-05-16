package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.ScheduleResult;
import com.kyalo.universitytimetabling.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<String> generateSchedule(@RequestParam int semester) {
        try {
            // Capture the ScheduleResult object returned by the service method
            ScheduleResult scheduleResult = scheduleService.generateSchedule(semester);

            // Extract the message from the ScheduleResult object
            String message = scheduleResult.getMessage();

            // Return the message as the body of the ResponseEntity
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred while generating the schedule: " + e.getMessage());
        }
    }

}
