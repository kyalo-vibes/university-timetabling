package com.kyalo.universitytimetabling.controller;

import com.kyalo.universitytimetabling.domain.TimeSlot;
import com.kyalo.universitytimetabling.domain.TimeSlotDTO;
import com.kyalo.universitytimetabling.service.TimeSlotService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @Autowired
    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(@RequestBody TimeSlotDTO timeSlotDto) {
        TimeSlot savedTimeSlot = timeSlotService.createTimeSlot(timeSlotDto);
        return new ResponseEntity<>(savedTimeSlot, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeSlotDTO> updateTimeSlot(@PathVariable Long id, @RequestBody TimeSlotDTO timeSlotDto) {
        TimeSlotDTO updatedTimeSlotDto = timeSlotService.updateTimeSlot(id, timeSlotDto);
        return new ResponseEntity<>(updatedTimeSlotDto, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable Long id) {
        timeSlotService.deleteTimeSlot(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeSlot> getTimeSlotById(@PathVariable Long id) {
        TimeSlot timeSlot = timeSlotService.getTimeSlotById(id).orElseThrow(() -> new EntityNotFoundException("TimeSlot not found with id: " + id));
        return new ResponseEntity<>(timeSlot, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<TimeSlotDTO>> getAllTimeSlots() {
        List<TimeSlotDTO> timeSlotDTOs = timeSlotService.getAllTimeSlots();
        return new ResponseEntity<>(timeSlotDTOs, HttpStatus.OK);
    }
}
