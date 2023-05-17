package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Program;
import com.kyalo.universitytimetabling.domain.ScheduleResult;
import com.kyalo.universitytimetabling.service.ProgramService;
import com.kyalo.universitytimetabling.service.ScheduleService;
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

    @GetMapping("/program/{programId}/year/{year}")
    public ResponseEntity<ScheduleResult> getScheduleByProgramAndYear(@PathVariable Long programId, @PathVariable int year, @RequestParam int semester) {
        try {
            Program program = programService.getProgramById(programId); // Assume that programService.getProgramById() method exists to fetch a Program by its ID
            ScheduleResult scheduleResult = scheduleService.generateYearlySchedule(semester, year, program);

            return ResponseEntity.ok(scheduleResult);
        } catch (Exception e) {
            // In case of a bad request or an exception, return an empty ResponseEntity
            return ResponseEntity.badRequest().build();
        }
    }
}
