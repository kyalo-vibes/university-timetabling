package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.*;
import com.kyalo.universitytimetabling.repository.*;
import jakarta.transaction.Transactional;
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
    private final ProgramRepository programRepository;


    public ScheduleService(CourseRepository courseRepository, RoomRepository roomRepository, TimeSlotRepository timeSlotRepository, ScheduleRepository scheduleRepository, ProgramRepository programRepository) {
        this.courseRepository = courseRepository;
        this.roomRepository = roomRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.scheduleRepository = scheduleRepository;
        this.programRepository = programRepository;
    }

    // Main scheduling method
    @Transactional
    public Map<String, List<ScheduleResult>> generateSchedule(int semester) {
        Map<String, List<ScheduleResult>> scheduleResults = new HashMap<>();

        List<Program> programs = programRepository.findAll();

        for (Program program : programs) {
            for (int year = 1; year <= 4; year++) { // Adjust this according to the number of years in your program
                ScheduleResult result = generateYearlySchedule(semester, year, program);
                String key = "Year " + year + " " + program.getName();
                if(!scheduleResults.containsKey(key)){
                    scheduleResults.put(key, new ArrayList<>());
                }
                scheduleResults.get(key).add(result);
            }
        }

        return scheduleResults;
    }

    // Add a new method to get schedules for a specific program and year
    public List<ScheduleResult> getSchedulesForProgramAndYear(String programName, int year) {
        List<ScheduleResult> results = new ArrayList<>();

        // Get all the schedules from the database
        List<Schedule> schedules = scheduleRepository.findAll();

        // Filter the schedules based on the program and year
        for (Schedule schedule : schedules) {
            Course course = schedule.getCourse();
            if (course.getProgram().getName().equals(programName) && course.getYear() == year) {
                results.add(new ScheduleResult(Arrays.asList(schedule), "Schedules for Year " + year + " " + programName));
            }
        }

        return results;
    }

    public Map<String, ScheduleResult> getSchedulesForProgramIdYearAndSemester(Long programId, int year, int semester) {
        Map<String, ScheduleResult> scheduleResults = new HashMap<>();

        // Get all the schedules from the database
        List<Schedule> schedules = scheduleRepository.findAll();

        // Initialize lists to store the multiple values for each ScheduleResult field
        List<String> courseCodes = new ArrayList<>();
        List<String> instructorNames = new ArrayList<>();
        List<String> roomNames = new ArrayList<>();
        List<String> timeSlots = new ArrayList<>();

        // Filter the schedules based on the programId, year, and semester
        for (Schedule schedule : schedules) {
            Course course = schedule.getCourse();
            if (course.getProgram().getId().equals(programId) && (course.getYear() == year) && (course.getSemester() == semester)) {
                // Add the values to the respective lists
                courseCodes.add(schedule.getCourse().getCourseCode());
                instructorNames.add(schedule.getCourse().getInstructor().getFirstName());
                roomNames.add(schedule.getRoom().getRoomName());
                timeSlots.add(schedule.getTimeSlot().getTime());
            }
        }

        // If there are any matching schedules, create a ScheduleResult and add it to the map
        if (!courseCodes.isEmpty()) {
            String key = "Year " + year + " " + schedules.get(0).getCourse().getProgram().getName(); // Use program name instead of ID
            ScheduleResult result = new ScheduleResult();
            result.setCourseCodes(courseCodes);
            result.setInstructorNames(instructorNames);
            result.setRoomNames(roomNames);
            result.setTimeSlots(timeSlots);
            result.setMessage(key);
            scheduleResults.put(key, result);
        }

        return scheduleResults;
    }


    public Map<String, ScheduleResult> getSchedulesForInstructorId(Long instructorId) {
        Map<String, ScheduleResult> scheduleResults = new HashMap<>();

        // Get all the schedules from the database
        List<Schedule> schedules = scheduleRepository.findAll();

        // Initialize lists to store the multiple values for each ScheduleResult field
        List<String> courseCodes = new ArrayList<>();
        List<String> roomNames = new ArrayList<>();
        List<String> timeSlots = new ArrayList<>();

        // Filter the schedules based on the instructorId
        for (Schedule schedule : schedules) {
            Instructor instructor = schedule.getCourse().getInstructor();
            if (instructor.getId().equals(instructorId)) {
                // Add the values to the respective lists
                courseCodes.add(schedule.getCourse().getCourseCode());
                roomNames.add(schedule.getRoom().getRoomName());
                timeSlots.add(schedule.getTimeSlot().getTime());
            }
        }

        // If there are any matching schedules, create a ScheduleResult and add it to the map
        if (!courseCodes.isEmpty()) {
            String key = "Instructor " + schedules.get(0).getCourse().getInstructor().getFirstName(); // Use instructor name instead of ID
            ScheduleResult result = new ScheduleResult();
            result.setCourseCodes(courseCodes);
            result.setInstructorNames(Arrays.asList(schedules.get(0).getCourse().getInstructor().getFirstName()));
            result.setRoomNames(roomNames);
            result.setTimeSlots(timeSlots);
            result.setMessage(key);
            scheduleResults.put(key, result);
        }

        return scheduleResults;
    }


    @Transactional
    public ScheduleResult generateYearlySchedule(int semester, int year, Program program) {
        List<Course> courses = courseRepository.findBySemesterAndYearAndProgram(semester, year, program);
        List<Room> rooms = roomRepository.findAll();
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        List<Schedule> schedules = new ArrayList<>();

        // Check if there are any courses to schedule
        if(courses.isEmpty()) {
            return new ScheduleResult(schedules, "No courses to schedule.");
        }

        // Create a copy of the courses list for backtracking purposes
        List<Course> coursesCopy = new ArrayList<>(courses);

        // Create a set to keep track of the years for which courses have been scheduled
        Set<Integer> scheduledYears = new HashSet<>();

        while (!coursesCopy.isEmpty()) {
            // Choose the most constrained course first
            Course mostConstrainedCourse = getMostConstrainedCourse(coursesCopy, schedules);

            if (mostConstrainedCourse == null) {
                // If no course can be scheduled, return the schedules generated so far
                return new ScheduleResult(schedules, "No valid schedule can be found for remaining courses");
            }

            if (assignCourseToSchedule(mostConstrainedCourse, rooms, timeSlots, schedules)) {
                // Only remove the scheduled course from the list if it was successfully scheduled
                coursesCopy.remove(mostConstrainedCourse);

                // Add the year of the course to the set of scheduled years
                scheduledYears.add(mostConstrainedCourse.getYear());
            } else {
                if (!backtrackScheduleHelper(coursesCopy, rooms, timeSlots, coursesCopy.indexOf(mostConstrainedCourse), schedules)) {
                    return new ScheduleResult(schedules, "No valid schedule can be found for course " + mostConstrainedCourse.getCourseName() + " onwards");
                }
            }
        }

        // Check if any courses have been scheduled
        boolean anyCoursesScheduled = !schedules.isEmpty();

        if(!anyCoursesScheduled) {
            return new ScheduleResult(schedules, "No courses were scheduled.");
        } else {
            return new ScheduleResult(schedules, "Schedule created successfully");
        }
    }



    @Transactional
    private boolean assignCourseToSchedule(Course course, List<Room> rooms, List<TimeSlot> timeSlots, List<Schedule> schedules) {
        // Assign course to a room and timeslot
        for (Room room : rooms) {
            for (TimeSlot timeSlot : timeSlots) {
                Section section = course.getSection();

                // Check if the section is not null before proceeding
                if (section == null) {
                    // handle this situation appropriately, e.g., log an error message, throw an exception, or skip this course
                    return false;
                }

                // Check if the section is fully scheduled, continue to the next iteration
                if (isSectionFullyScheduled(section, schedules)) {
                    continue;
                }

                // Check if the room and time slot are valid for the course
                // Also check if the room's name contains the course's year
                if (room.isAvailable(timeSlot) && !instructorBusyAt(course.getInstructor(), timeSlot, schedules) &&
                        !studentBusyAt(course, timeSlot, schedules) && roomBelongsToDept(course, room) &&
                        room.getRoomName().contains(Integer.toString(course.getYear()))) {

                    Schedule schedule = new Schedule();
                    schedule.setCourse(course);
                    schedule.setRoom(room);
                    schedule.setTimeSlot(timeSlot);
                    schedule.setSection(section);

                    // Make room unavailable
                    room.occupyTimeSlot(timeSlot);

                    // Add the schedule to the schedule repository
                    scheduleRepository.save(schedule);

                    // Add the created schedule to the schedules list
                    schedules.add(schedule);

                    // Check for any constraint violations
                    if (evaluateSchedule(schedules) >= 0) {
                        // If the course has been scheduled for the number of classes required, return true
                        if (getScheduledClassesForSection(section, schedules) >= section.getNumberOfClasses()) {
                            return true;
                        } else {
                            // Continue scheduling the course until it has been scheduled for the required number of classes
                            continue;
                        }
                    } else {
                        // If the schedule is not valid, remove it from the schedule repository and the schedules list
                        scheduleRepository.delete(schedule);
                        schedules.remove(schedule);

                        // Make room available again
                        room.freeTimeSlot(timeSlot);
                    }
                }
            }
        }

        // Check if there are no valid rooms or time slots for the course
        if (getValidRoomsForCourse(course, schedules).isEmpty()) {
            throw new RuntimeException("No valid rooms for the course " + course.getCourseName());
        }
        if (getValidTimeSlotsForCourse(course, schedules).isEmpty()) {
            throw new RuntimeException("No valid time slots for the course " + course.getCourseName());
        }

        // If we made it here, it means we couldn't assign the course to a schedule.
        // We should return false here to signal that we couldn't find a valid schedule for this course.
        return false;
    }

    // Add this new helper method to get the number of scheduled classes for a section
    private int getScheduledClassesForSection(Section section, List<Schedule> schedules) {
        int count = 0;
        for (Schedule schedule : schedules) {
            if (schedule.getSection().equals(section)) {
                count++;
            }
        }
        return count;
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
    @Transactional
    public List<Schedule> backtrackingSchedule(List<Course> courses, List<Room> rooms, List<TimeSlot> timeSlots) {
        // Create a new list of schedules
        List<Schedule> schedules = new ArrayList<>();

        // Call the recursive helper function to fill in the schedules
        if (backtrackScheduleHelper(courses, rooms, timeSlots, 0, schedules)) {
            return schedules;
        }

        // If no valid schedule was found, return null
        return null;
    }


    @Transactional
    private boolean backtrackScheduleHelper(List<Course> courses, List<Room> rooms, List<TimeSlot> timeSlots, int courseIndex, List<Schedule> schedules) {
        // If we've assigned all courses, the schedule is complete
        if (courseIndex == courses.size()) {
            return true;
        }

        Course course = courses.get(courseIndex);

        // Iterate over all rooms and time slots
        for (Room room : rooms) {
            for (TimeSlot timeSlot : timeSlots) {
                // Create a new Schedule for this assignment
                Schedule schedule = new Schedule();

                // Check if the room and time slot are valid for the course
                if (room.isAvailable(timeSlot) && !instructorBusyAt(course.getInstructor(), timeSlot, schedules) &&
                        !studentBusyAt(course, timeSlot, schedules) && roomBelongsToDept(course, room)) {

                    // Assign the course to the room and time slot
                    schedule.setCourse(course);
                    schedule.setRoom(room);
                    schedule.setTimeSlot(timeSlot);

                    // Make room unavailable
                    room.occupyTimeSlot(timeSlot);

                    // Add the schedule to the schedule repository and the current schedules list
                    scheduleRepository.save(schedule);
                    schedules.add(schedule);

                    // Recursively assign the rest of the courses
                    if (backtrackScheduleHelper(courses, rooms, timeSlots, courseIndex + 1, schedules)) {
                        return true;
                    }

                    // If assigning the course to the room and time slot didn't lead to a solution, remove it
                    scheduleRepository.delete(schedule);
                    schedules.remove(schedule);

                    // Make room available again
                    room.freeTimeSlot(timeSlot);
                }
            }
        }

        // If no room and time slot could be found for the course, backtrack by returning false
        // This will trigger the backtracking process in the previous recursion level
        return false;
    }



    private boolean instructorBusyAt(Instructor instructor, TimeSlot timeSlot, List<Schedule> schedules) {
        // Check if the instructor is already teaching a course at the given time slot
        for(Schedule schedule : schedules) {
            if(schedule.getCourse().getInstructor().equals(instructor) && schedule.getTimeSlot().equals(timeSlot)) {
                return true;
            }
        }
        return false;
    }

    private boolean studentBusyAt(Course course, TimeSlot timeSlot, List<Schedule> schedules) {
        // Check if the students are already taking a course at the given time slot
        for(Schedule schedule : schedules) {
            if(schedule.getCourse().getProgram().equals(course.getProgram()) && schedule.getTimeSlot().equals(timeSlot)) {
                return true;
            }
        }
        return false;
    }

    private boolean roomBelongsToDept(Course course, Room room) {
        // Check if the room belongs to the department of the course
        // This implementation assumes that a method getRooms() exists in the Department class
        return course.getDepartment().getRooms().contains(room);
    }

    private boolean isSectionFullyScheduled(Section section, List<Schedule> schedules) {
        int classesScheduled = 0;

        // Loop over the schedules and count how many times this section appears
        for (Schedule schedule : schedules) {
            if (schedule.getCourse().getSection().equals(section)) {
                classesScheduled++;
            }
        }

        // Compare the number of scheduled classes with the section's numberOfClasses
        return classesScheduled >= section.getNumberOfClasses();
    }


}