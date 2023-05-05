package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.Schedule;
import com.kyalo.universitytimetabling.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {

    private final SchedulingService schedulingService;

    @Autowired
    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @GetMapping("/generate")
    public ResponseEntity<List<Schedule>> generateSchedule() {
        List<Schedule> generatedSchedule = schedulingService.generateSchedule();
        return ResponseEntity.ok(generatedSchedule);
    }
}
