package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.*;
import com.kyalo.universitytimetabling.service.CourseService;
import com.kyalo.universitytimetabling.service.InstructorService;
import com.kyalo.universitytimetabling.service.RoomService;
import com.kyalo.universitytimetabling.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SchedulingService {

    private final CourseService courseService;
    private final InstructorService instructorService;
    private final RoomService roomService;
    private final TimeSlotService timeSlotService;

    @Autowired
    public SchedulingService(CourseService courseService,
                             InstructorService instructorService,
                             RoomService roomService,
                             TimeSlotService timeSlotService) {
        this.courseService = courseService;
        this.instructorService = instructorService;
        this.roomService = roomService;
        this.timeSlotService = timeSlotService;
    }

    public List<Schedule> generateSchedule() {
        List<Course> courses = courseService.getAllCourses();
        List<Instructor> instructors = instructorService.getAllInstructors();
        List<Room> rooms = roomService.getAllRooms();
        List<TimeSlot> timeSlots = timeSlotService.getAllTimeSlots();

        List<Schedule> generatedSchedule = new ArrayList<>();
        boolean result = scheduleCourses(0, courses, rooms, timeSlots, generatedSchedule);

        if (result) {
            return generatedSchedule;
        } else {
            throw new RuntimeException("Failed to generate a schedule");
        }
    }

    private boolean scheduleCourses(int courseIndex, List<Course> courses, List<Room> rooms,
                                    List<TimeSlot> timeSlots, List<Schedule> generatedSchedule) {
        if (courseIndex >= courses.size()) {
            return true;
        }

        Course course = courses.get(courseIndex);
        Collections.shuffle(timeSlots); // Randomize the time slots for different schedules

        for (TimeSlot timeSlot : timeSlots) {
            for (Room room : rooms) {
                Schedule schedule = new Schedule(course, room, timeSlot);

                if (isScheduleValid(schedule, generatedSchedule)) {
                    generatedSchedule.add(schedule);

                    if (scheduleCourses(courseIndex + 1, courses, rooms, timeSlots, generatedSchedule)) {
                        return true;
                    } else {
                        generatedSchedule.remove(schedule);
                    }
                }
            }
        }

        return false;
    }

    private boolean isScheduleValid(Schedule schedule, List<Schedule> generatedSchedule) {
        HardConstraint hardConstraint = new HardConstraint();
        SoftConstraint softConstraint = new SoftConstraint();

        for (Schedule existingSchedule : generatedSchedule) {
            if (!hardConstraint.validate(schedule, existingSchedule)) {
                return false;
            }
        }

        return softConstraint.validate(schedule, generatedSchedule);
    }

    public class HardConstraint {
        public boolean validate(Schedule schedule, Schedule existingSchedule) {
            if (existingSchedule.getTimeSlot().equals(schedule.getTimeSlot())) {
                if (existingSchedule.getRoom().equals(schedule.getRoom())) {
                    return false; // Room is already occupied
                }

                if (existingSchedule.getCourse().getInstructor().equals(schedule.getCourse().getInstructor())) {
                    return false; // Instructor is already scheduled
                }
            }

            return true;
        }
    }

    public class SoftConstraint {
        private static final int MAX_TIME_GAP = 2; // The maximum allowed time gap between classes

        public boolean validate(Schedule schedule, List<Schedule> generatedSchedule) {
            return isPreferredTimeSlot(schedule) && hasMinimalTimeGap(schedule, generatedSchedule);
        }

        private boolean isPreferredTimeSlot(Schedule schedule) {
            Instructor instructor = schedule.getCourse().getInstructor();
            TimeSlot timeSlot = schedule.getTimeSlot();

            // Check if the instructor has a preference for the current time slot
            if (instructor.getPreferences() == null || instructor.getPreferences().isEmpty()) {
                return true; // No preferences specified, any time slot is acceptable
            }

            return instructor.getPreferences().contains(timeSlot);
        }

        private boolean hasMinimalTimeGap(Schedule schedule, List<Schedule> generatedSchedule) {
            Instructor instructor = schedule.getCourse().getInstructor();
            TimeSlot currentTimeSlot = schedule.getTimeSlot();

            for (Schedule existingSchedule : generatedSchedule) {
                if (existingSchedule.getCourse().getInstructor().equals(instructor)) {
                    int timeGap = Math.abs((int) (existingSchedule.getTimeSlot().getId() - currentTimeSlot.getId()));

                    if (timeGap <= MAX_TIME_GAP) {
                        return true;
                    }
                }
            }

            return false;
        }
    }


}
