package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.*;
import com.kyalo.universitytimetabling.repository.CourseRepository;
import com.kyalo.universitytimetabling.repository.RoomRepository;
import com.kyalo.universitytimetabling.repository.ScheduleRepository;
import com.kyalo.universitytimetabling.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    // Assuming that these repositories have been implemented
    private final CourseRepository courseRepository;
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(CourseRepository courseRepository, RoomRepository roomRepository, TimeSlotRepository timeSlotRepository, ScheduleRepository scheduleRepository) {
        this.courseRepository = courseRepository;
        this.roomRepository = roomRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // Main scheduling method
    public void generateSchedule() {
        // Retrieve all courses, rooms and timeslots
        List<Course> courses = courseRepository.findAll();
        List<Room> rooms = roomRepository.findAll();
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();

        // Sort the courses based on the heuristic function
        courses.sort(Comparator.comparing(c -> getValidTimeSlotsForCourse(c, scheduleRepository.findAll()).size() * getValidRoomsForCourse(c, scheduleRepository.findAll()).size()));

        // Assign courses to rooms and timeslots
        for (Course course : courses) {
            // Get valid rooms and timeslots for the course
            List<Room> validRooms = getValidRoomsForCourse(course, scheduleRepository.findAll());
            List<TimeSlot> validTimeSlots = getValidTimeSlotsForCourse(course, scheduleRepository.findAll());

            boolean scheduled = false;

            // Assign course to a room and timeslot
            for (Room room : validRooms) {
                for (TimeSlot timeSlot : validTimeSlots) {
                    Schedule schedule = new Schedule();
                    schedule.setCourse(course);
                    schedule.setRoom(room);
                    schedule.setTimeSlot(timeSlot);

                    // Add the schedule to the schedule repository
                    scheduleRepository.save(schedule);

                    // Check for any constraint violations
                    if (evaluateSchedule(scheduleRepository.findAll()) >= 0) {
                        scheduled = true;
                        break;
                    } else {
                        // If the schedule is not valid, remove it from the schedule repository
                        scheduleRepository.delete(schedule);
                    }
                }
                if (scheduled) {
                    break;
                }
            }

            // If the course could not be scheduled, backtrack
            if (!scheduled) {
                backtrackingSchedule(courses, rooms, timeSlots);
            }
        }
    }


    // Define heuristic function here
    // Heuristic Function
    private Course getMostConstrainedCourse(List<Course> courses, List<Schedule> schedules) {
        Course mostConstrainedCourse = null;
        int minValidTimeSlots = Integer.MAX_VALUE;
        int minValidRooms = Integer.MAX_VALUE;

        for (Course course : courses) {
            int validTimeSlots = getValidTimeSlotsForCourse(course, schedules).size();
            int validRooms = getValidRoomsForCourse(course, schedules).size();

            if (validTimeSlots < minValidTimeSlots && validRooms < minValidRooms) {
                minValidTimeSlots = validTimeSlots;
                minValidRooms = validRooms;
                mostConstrainedCourse = course;
            }
        }

        return mostConstrainedCourse;
    }

    private List<TimeSlot> getValidTimeSlotsForCourse(Course course, List<Schedule> schedules) {
        // Get all the time slots from the database.
        List<TimeSlot> allTimeSlots = timeSlotRepository.findAll();

        // Create a list to store valid time slots.
        List<TimeSlot> validTimeSlots = new ArrayList<>(allTimeSlots);

        // Iterate over the schedules.
        for (Schedule schedule : schedules) {
            // If the course of the current schedule is the same as the given course,
            // remove the time slot of the current schedule from the valid time slots.
            if (schedule.getCourse().equals(course)) {
                validTimeSlots.remove(schedule.getTimeSlot());
            }
        }

        return validTimeSlots;
    }

    private List<Room> getValidRoomsForCourse(Course course, List<Schedule> schedules) {
        // Get all the rooms from the database.
        List<Room> allRooms = roomRepository.findAll();

        // Create a list to store valid rooms.
        List<Room> validRooms = new ArrayList<>(allRooms);

        // Iterate over the schedules.
        for (Schedule schedule : schedules) {
            // If the course of the current schedule is the same as the given course,
            // remove the room of the current schedule from the valid rooms.
            if (schedule.getCourse().equals(course)) {
                validRooms.remove(schedule.getRoom());
            }
        }

        return validRooms;
    }



    // Define evaluation function here
    private int evaluateSchedule(List<Schedule> schedules) {
        int score = 0;

        // Check each schedule
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);

            // Check for room availability
            if (!schedule.getRoom().isAvailable()) {
                score--;
            }

            // Check for instructor availability
            for (int j = i + 1; j < schedules.size(); j++) {
                Schedule otherSchedule = schedules.get(j);

                // If the schedules have the same instructor and time slot, it's a conflict
                if (schedule.getCourse().getInstructor().equals(otherSchedule.getCourse().getInstructor()) &&
                        schedule.getTimeSlot().equals(otherSchedule.getTimeSlot())) {
                    score--;
                }
            }
        }

        return score;
    }

    // Define backtracking function here
    public Schedule backtrackingSchedule(List<Course> courses, List<Room> rooms, List<TimeSlot> timeSlots) {
        // Create a new schedule
        Schedule schedule = new Schedule();

        // Call the recursive helper function to fill in the schedule
        if (backtrackScheduleHelper(courses, rooms, timeSlots, 0, schedule)) {
            return schedule;
        }

        // If no valid schedule was found, return null
        return null;
    }

    private boolean backtrackScheduleHelper(List<Course> courses, List<Room> rooms, List<TimeSlot> timeSlots, int courseIndex, Schedule schedule) {
        // If we've assigned all courses, the schedule is complete
        if (courseIndex == courses.size()) {
            return true;
        }

        Course course = courses.get(courseIndex);

        // Iterate over all rooms and time slots
        for (Room room : rooms) {
            for (TimeSlot timeSlot : timeSlots) {
                // Check if the room and time slot are valid for the course
                if (room.isAvailable() && !instructorBusyAt(course.getInstructor(), timeSlot, schedule) &&
                        !studentBusyAt(course, timeSlot, schedule) && roomBelongsToDept(course, room)) {

                    // Assign the course to the room and time slot
                    schedule.setCourse(course);
                    schedule.setRoom(room);
                    schedule.setTimeSlot(timeSlot);

                    // Make room unavailable
                    room.setAvailable(false);

                    // Recursively assign the rest of the courses
                    if (backtrackScheduleHelper(courses, rooms, timeSlots, courseIndex + 1, schedule)) {
                        return true;
                    }

                    // If assigning the course to the room and time slot didn't lead to a solution, remove it
                    schedule.setCourse(null);
                    schedule.setRoom(null);
                    schedule.setTimeSlot(null);

                    // Make room available again
                    room.setAvailable(true);
                }
            }
        }

        // If no room and time slot could be found for the course, return false
        return false;
    }

    private boolean instructorBusyAt(Instructor instructor, TimeSlot timeSlot, Schedule schedule) {
        // Check if the instructor is already teaching a course at the given time slot
        return schedule.getCourse().getInstructor().equals(instructor) && schedule.getTimeSlot().equals(timeSlot);
    }

    private boolean studentBusyAt(Course course, TimeSlot timeSlot, Schedule schedule) {
        // Check if the students are already taking a course at the given time slot
        return schedule.getCourse().getProgram().equals(course.getProgram()) && schedule.getTimeSlot().equals(timeSlot);
    }

    private boolean roomBelongsToDept(Course course, Room room) {
        // Check if the room belongs to the department of the course
        // This implementation assumes that a method getRooms() exists in the Department class
        return course.getDepartment().getRooms().contains(room);
    }



}
