package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.TimeSlot;
import com.kyalo.universitytimetabling.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }

    public Optional<TimeSlot> getTimeSlotById(Long id) {
        return timeSlotRepository.findById(id);
    }

    public TimeSlot createTimeSlot(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    public TimeSlot updateTimeSlot(TimeSlot timeSlot) {
        Optional<TimeSlot> timeSlotOptional = timeSlotRepository.findById(timeSlot.getId());
        if (timeSlotOptional.isPresent()) {
            return timeSlotRepository.save(timeSlot);
        } else {
            throw new EntityNotFoundException("TimeSlot not found with id: " + timeSlot.getId());
        }
    }


    public void deleteTimeSlot(Long id) {
        timeSlotRepository.deleteById(id);
    }
}
