package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Schedule;
import com.kyalo.universitytimetabling.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
public class SchedulingController {

    private final SchedulingService schedulingService;

    @Autowired
    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @GetMapping("/generate/{semester}")
    public ResponseEntity<Map<String, Map<Integer, List<Schedule>>>> generateScheduleForAllYearsAndPrograms(@PathVariable("semester") int semester) {
        Map<String, Map<Integer, List<Schedule>>> schedules = schedulingService.generateScheduleForAllYearsAndPrograms(semester);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }
}
