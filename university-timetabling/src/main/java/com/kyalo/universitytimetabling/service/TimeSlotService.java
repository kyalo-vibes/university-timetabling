package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.TimeSlot;
import com.kyalo.universitytimetabling.domain.TimeSlotDTO;
import com.kyalo.universitytimetabling.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<TimeSlotDTO> getAllTimeSlots() {
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();

        List<TimeSlotDTO> timeSlotDTOs = timeSlots.stream()
                .map(timeSlot -> new TimeSlotDTO(
                        timeSlot.getId(),
                        timeSlot.getDay(),
                        timeSlot.getStartTime(),
                        timeSlot.getEndTime())
                )
                .collect(Collectors.toList());

        return timeSlotDTOs;
    }
    public Optional<TimeSlot> getTimeSlotById(Long id) {
        return timeSlotRepository.findById(id);
    }

    public TimeSlot createTimeSlot(TimeSlotDTO timeSlotDto) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDay(timeSlotDto.getDay());
        timeSlot.setStartTime(timeSlotDto.getStartTime());
        timeSlot.setEndTime(timeSlotDto.getEndTime());

        return timeSlotRepository.save(timeSlot);
    }
    public TimeSlotDTO updateTimeSlot(Long id, TimeSlotDTO timeSlotDto) {
        Optional<TimeSlot> timeSlotOptional = timeSlotRepository.findById(id);
        if (timeSlotOptional.isPresent()) {
            TimeSlot timeSlot = timeSlotOptional.get();
            timeSlot.setDay(timeSlotDto.getDay());
            timeSlot.setStartTime(timeSlotDto.getStartTime());
            timeSlot.setEndTime(timeSlotDto.getEndTime());

            TimeSlot updatedTimeSlot = timeSlotRepository.save(timeSlot);

            return new TimeSlotDTO(updatedTimeSlot.getId(), updatedTimeSlot.getDay(), updatedTimeSlot.getStartTime(), updatedTimeSlot.getEndTime());
        } else {
            throw new EntityNotFoundException("TimeSlot not found with id: " + id);
        }
    }



    public void deleteTimeSlot(Long id) {
        timeSlotRepository.deleteById(id);
    }
}
