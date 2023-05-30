package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.*;
import com.kyalo.universitytimetabling.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.javapoet.ClassName;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
public class ScheduleService {

    private final CourseRepository courseRepository;
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ScheduleRepository scheduleRepository;
    private final ProgramRepository programRepository;
    private final ScheduleResultRepository scheduleResultRepository;
    private final UserRepository userRepository;
    private final ProgramEnrollmentRepository programEnrollmentRepository;
    private final ScheduleStatusRepository scheduleStatusRepository;
    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName()); // Replace ClassName with the actual class name

    private Map<String, List<Schedule>> commonCourseSchedules = new HashMap<>();



    public ScheduleService(CourseRepository courseRepository, RoomRepository roomRepository, TimeSlotRepository timeSlotRepository, ScheduleRepository scheduleRepository, ProgramRepository programRepository, ScheduleResultRepository scheduleResultRepository, UserRepository userRepository, ProgramEnrollmentRepository programEnrollmentRepository, ScheduleStatusRepository scheduleStatusRepository) {
        this.courseRepository = courseRepository;
        this.roomRepository = roomRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.scheduleRepository = scheduleRepository;
        this.programRepository = programRepository;
        this.scheduleResultRepository = scheduleResultRepository;
        this.userRepository = userRepository;
        this.programEnrollmentRepository = programEnrollmentRepository;
        this.scheduleStatusRepository = scheduleStatusRepository;
    }

    // Main scheduling method
    @Transactional
    @Async
    public void generateSchedule(int semester) {
        ScheduleStatus scheduleStatus = new ScheduleStatus();
        scheduleStatus.setSemester(semester);
        scheduleStatus.setStatus("IN_PROGRESS");
        scheduleStatusRepository.save(scheduleStatus);
        Map<String, List<ScheduleResult>> scheduleResults = new HashMap<>();
        List<ScheduleResult> resultsToSave = new ArrayList<>();

        List<Program> programs = programRepository.findAll();

        for (Program program : programs) {
            for (int year = 1; year <= 4; year++) {
                ScheduleResult result = generateYearlySchedule(semester, year, program);
                String key = "Year " + year + " " + program.getName();
                if(!scheduleResults.containsKey(key)){
                    scheduleResults.put(key, new ArrayList<>());
                }
                scheduleResults.get(key).add(result);
                resultsToSave.add(result);  // Add the result to the list that will be saved
            }
        }

        // Save the generated schedules to a database
        scheduleResultRepository.saveAll(resultsToSave);

        // Update the status as COMPLETED
        scheduleStatus.setStatus("COMPLETED");
        scheduleStatusRepository.save(scheduleStatus);
    }

    public List<ScheduleResult> getAllScheduleResults() {
        return scheduleResultRepository.findAll();
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

    public Map<String, ScheduleResult> getSchedulesForLoggedInUser(String username) {
        Map<String, ScheduleResult> scheduleResults = new HashMap<>();

        // Get the student for the current user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Student student = user.getStudent();

        Long programId = student.getProgram().getId();
        int year = student.getYear();

        // Get all the schedules from the database
        List<Schedule> schedules = scheduleRepository.findAll();

        // Initialize lists to store the multiple values for each ScheduleResult field
        List<String> courseCodes = new ArrayList<>();
        List<String> instructorNames = new ArrayList<>();
        List<String> roomNames = new ArrayList<>();
        List<String> timeSlots = new ArrayList<>();

        // Filter the schedules based on the programId and year
        for (Schedule schedule : schedules) {
            Course course = schedule.getCourse();
            if (course.getProgram().getId().equals(programId) && (course.getYear() == year)) {
                // Add the values to the respective lists
                courseCodes.add(schedule.getCourse().getCourseCode());
                instructorNames.add(schedule.getCourse().getInstructor().getFirstName());
                roomNames.add(schedule.getRoom().getRoomName());

                // Add the day of the week to the time slot
                String timeSlotWithDay = schedule.getTimeSlot().getDay() + " " + schedule.getTimeSlot().getTime();
                timeSlots.add(timeSlotWithDay);
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



    public Map<String, ScheduleResult> getSchedulesForInstructor(String username) {
        Map<String, ScheduleResult> scheduleResults = new HashMap<>();

        // Get the instructor for the current user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Instructor instructor = user.getInstructor();
        Long instructorId = instructor.getId();

        // Get all the schedules from the database
        List<Schedule> schedules = scheduleRepository.findAll();

        // Initialize lists to store the multiple values for each ScheduleResult field
        List<String> courseCodes = new ArrayList<>();
        List<String> roomNames = new ArrayList<>();
        List<String> timeSlots = new ArrayList<>();

        // Filter the schedules based on the instructorId
        for (Schedule schedule : schedules) {
            Instructor scheduleInstructor = schedule.getCourse().getInstructor();
            if (scheduleInstructor.getId().equals(instructorId)) {
                // Add the values to the respective lists
                courseCodes.add(schedule.getCourse().getCourseCode());
                roomNames.add(schedule.getRoom().getRoomName());

                // Add the day of the week to the time slot
                String timeSlotWithDay = schedule.getTimeSlot().getDay() + " " + schedule.getTimeSlot().getTime();
                timeSlots.add(timeSlotWithDay);
            }
        }

        // If there are any matching schedules, create a ScheduleResult and add it to the map
        if (!courseCodes.isEmpty()) {
            String key = "Instructor " + instructor.getFirstName(); // Use instructor name instead of ID
            ScheduleResult result = new ScheduleResult();
            result.setCourseCodes(courseCodes);
            result.setInstructorNames(Arrays.asList(instructor.getFirstName()));
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
            // First, iterate through the instructor's preferred time slots
            Set<TimeSlot> preferredTimeSlots = course.getInstructor().getPreferences();
            for (TimeSlot timeSlot : preferredTimeSlots) {
                if (tryAssignCourseToTimeSlot(course, room, timeSlot, schedules)) {
                    return true;
                }
            }

            // Then, iterate through all other time slots
            for (TimeSlot timeSlot : timeSlots) {
                // Skip the preferred time slots since they have already been checked
                if (!preferredTimeSlots.contains(timeSlot)) {
                    if (tryAssignCourseToTimeSlot(course, room, timeSlot, schedules)) {
                        return true;
                    }
                }
            }
        }

        // If we made it here, it means we couldn't assign the course to a schedule.
        // We should return false here to signal that we couldn't find a valid schedule for this course.
        return false;
    }

    private boolean tryAssignCourseToTimeSlot(Course course, Room room, TimeSlot timeSlot, List<Schedule> schedules) {
        Section section = course.getSection();

        // Check if the section is not null before proceeding
        if (section == null) {
            LOGGER.severe("Section is null for course: " + course.getId()); // Replace getId() with your method to get unique identifier for course
            return false;
        }

        // Check if the course is a common course
        if (course.isCommonCourse()) {
            List<Schedule> existingCommonCourseSchedules = commonCourseSchedules.get(course.getCommonId());

            // If this common course has been scheduled before, fetch the schedules
            if (existingCommonCourseSchedules != null && existingCommonCourseSchedules.size() >= section.getNumberOfClasses()) {
                for (Schedule existingSchedule : existingCommonCourseSchedules) {
                    Schedule newSchedule = new Schedule(existingSchedule); // assuming there's a copy constructor
                    newSchedule.setCourse(course); // update the course id to the current one
                    // save the schedule in the repository
                    scheduleRepository.save(newSchedule);
                    // add this schedule to the schedules list
                    schedules.add(newSchedule);
                }
                LOGGER.info("Common course " + course.getId() + " is fully scheduled.");
                return true;
            } else {
                // The common course is being scheduled for the first time
                int requiredClasses = section.getNumberOfClasses();
                int classesScheduled = 0;

                // Populate the available rooms and time slots
                List<Room> availableRooms = getValidRoomsForCourse(course, schedules);
                List<TimeSlot> availableTimeSlots = getValidTimeSlotsForCourse(course, schedules);

                // Iterate through available rooms and timeslots
                for (Room availRoom : availableRooms) {
                    for (TimeSlot availTimeSlot : availableTimeSlots) {
                        // Check if the room and time slot are valid for the course
                        // Also check if the room's name contains the course's year
                        if (availRoom.isAvailable(availTimeSlot) && !instructorBusyAt(course.getInstructor(), availTimeSlot, schedules) &&
                                !studentBusyAt(course, availTimeSlot, schedules) && !timeSlotBusyAt(course, availTimeSlot, schedules) && roomBelongsToDept(course, availRoom) &&
                                roomMatchesCourseSpec(course, availRoom) && availRoom.getRoomName().contains(Integer.toString(course.getYear()))) {

                            Schedule schedule = new Schedule();
                            schedule.setCourse(course);
                            schedule.setRoom(availRoom);
                            schedule.setTimeSlot(availTimeSlot);
                            schedule.setSection(section);

                            // Make room unavailable
                            availRoom.occupyTimeSlot(availTimeSlot);

                            // Add the schedule to the schedule repository
                            scheduleRepository.save(schedule);

                            // Add the created schedule to the schedules list
                            schedules.add(schedule);

                            // Store this schedule for this common course
                            commonCourseSchedules.computeIfAbsent(course.getCommonId(), k -> new ArrayList<>()).add(schedule);

                            // Increase scheduled class count
                            classesScheduled++;

                            // If the course has been scheduled for the number of classes required, return true
                            if (classesScheduled >= requiredClasses) {
                                return true;
                            }
                        }
                        // If we've scheduled all required classes, break the outer loop as well
                        if (classesScheduled >= requiredClasses) {
                            break;
                        }
                    }

                    // If the common course is not fully scheduled after the loop, it means it failed to be scheduled and should return false.
                    return false;
                }
            }
        }


        // Check if the section is fully scheduled, continue to the next iteration
        if (isSectionFullyScheduled(section, schedules)) {
            LOGGER.info("Section " + section.getId() + " is fully scheduled."); // Replace getId() with your method to get unique identifier for section
            return false;
        }

        ProgramEnrollment programEnrollment = programEnrollmentRepository.findByProgramAndYear(course.getProgram(), course.getYear());
        if (programEnrollment != null && room.getCapacity() < programEnrollment.getEnrolledNumber()) {
            LOGGER.warning("Room " + room.getId() + " capacity is less than the enrolled number."); // Replace getId() with your method to get unique identifier for room
            return false;
        }

        // Check if the room and time slot are valid for the course
        // Also check if the room's name contains the course's year
        if (room.isAvailable(timeSlot) && !instructorBusyAt(course.getInstructor(), timeSlot, schedules) &&
                !studentBusyAt(course, timeSlot, schedules) && !timeSlotBusyAt(course, timeSlot, schedules) && roomBelongsToDept(course, room) &&
                roomMatchesCourseSpec(course, room) &&
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

            // Store this schedule for this common course
            if (course.isCommonCourse()) {
                if (!commonCourseSchedules.containsKey(course.getCommonId())) {
                    commonCourseSchedules.put(course.getCommonId(), new ArrayList<>());
                }
                commonCourseSchedules.get(course.getCommonId()).add(schedule);
            }

            // Check for any constraint violations
            if (evaluateSchedule(schedules) >= 0) {
                // If the course has been scheduled for the number of classes required, return true
                if (getScheduledClassesForSection(section, schedules) >= section.getNumberOfClasses()) {
                    return true;
                } else {
                    // Continue scheduling the course until it has been scheduled for the required number of classes
                    return false;
                }
            } else {
                // If the schedule is not valid, remove it from the schedule repository and the schedules list
                scheduleRepository.delete(schedule);
                schedules.remove(schedule);

                // Make room available again
                room.freeTimeSlot(timeSlot);
            }
        }

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
        List<Room> allRooms = roomRepository.findAll();
        List<Room> validRooms = new ArrayList<>();

        for (Room room : allRooms) {
            if (roomMatchesCourseSpec(course, room)) {
                validRooms.add(room);
            }
        }

        for (Schedule schedule : schedules) {
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
            LOGGER.info("All courses have been assigned. Schedule is complete.");
            return true;
        }

        Course course = courses.get(courseIndex);

        // Check if the course is a common course
        if (course.isCommonCourse()) {
            // If the common course has been scheduled before, use the existing schedules
            if (commonCourseSchedules.containsKey(course.getCommonId())) {
                List<Schedule> existingSchedules = commonCourseSchedules.get(course.getCommonId());

                // Add these schedules to the current schedules list
                schedules.addAll(existingSchedules);

                // Continue with the next course;
                return backtrackScheduleHelper(courses, rooms, timeSlots, courseIndex + 1, schedules);
            }
        }

        // Iterate over all rooms and time slots
        for (Room room : rooms) {
            for (TimeSlot timeSlot : timeSlots) {
                // Create a new Schedule for this assignment
                Schedule schedule = new Schedule();

                // If the room and time slot are valid for the course
                if (room.isAvailable(timeSlot) && !instructorBusyAt(course.getInstructor(), timeSlot, schedules) &&
                        !studentBusyAt(course, timeSlot, schedules) && !timeSlotBusyAt(course, timeSlot, schedules) &&
                        roomBelongsToDept(course, room)) {

                    // Assign the course to the room and time slot
                    schedule.setCourse(course);
                    schedule.setRoom(room);
                    schedule.setTimeSlot(timeSlot);
                    schedule.setSection(course.getSection()); // Set the section for the schedule

                    // Make room unavailable
                    room.occupyTimeSlot(timeSlot);

                    // Add the schedule to the schedule repository and the current schedules list
                    scheduleRepository.save(schedule);
                    schedules.add(schedule);
                    LOGGER.info("Schedule saved and added to schedules. Room made unavailable.");

                    // If the course is a common course, add it to the commonCourseSchedules map
                    if (course.isCommonCourse()) {
                        List<Schedule> existingSchedules = commonCourseSchedules.getOrDefault(course.getCommonId(), new ArrayList<>());
                        existingSchedules.add(schedule);
                        commonCourseSchedules.put(course.getCommonId(), existingSchedules);
                    }

                    // Recursively assign the rest of the courses
                    if (backtrackScheduleHelper(courses, rooms, timeSlots, courseIndex + 1, schedules)) {
                        return true;
                    }

                    // If assigning the course to the room and time slot didn't lead to a solution, remove it
                    scheduleRepository.delete(schedule);
                    schedules.remove(schedule);
                    LOGGER.warning("Assignment didn't lead to a solution. Schedule removed and room made available.");

                    // Make room available again
                    room.freeTimeSlot(timeSlot);

                    // If the course is a common course and the assignment didn't lead to a solution, remove it from the commonCourseSchedules map
                    if (course.isCommonCourse()) {
                        commonCourseSchedules.get(course.getCommonId()).remove(schedule);
                    }
                }
            }
        }

        // If no room and time slot could be found for the course, backtrack by returning false
        // This will trigger the backtracking process in the previous recursion level
        LOGGER.warning("No room and time slot found for the course. Backtracking...");
        return false;
    }


    private boolean timeSlotBusyAt(Course currentCourse, TimeSlot timeSlot, List<Schedule> schedules) {
        // Check if the time slot is already assigned to another course in the same program
        for (Schedule schedule : schedules) {
            if (schedule.getTimeSlot().equals(timeSlot) &&
                    schedule.getCourse().getProgram().equals(currentCourse.getProgram())) {
                return true;
            }
        }
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
        for(Schedule schedule : schedules) {
            if(schedule.getCourse().getCourseCode().equals(course.getCourseCode()) && schedule.getTimeSlot().equals(timeSlot)) {
                return true;
            }
        }
        return false;
    }


    private boolean roomBelongsToDept(Course course, Room room) {
        // Check if the room belongs to the department of the course
        // This implementation assumes that a method getDepartments() exists in the Room class
        return room.getDepartments().contains(course.getDepartment());
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

    private boolean roomMatchesCourseSpec(Course course, Room room) {
        // If the course has a room_spec, check if it matches the room's type.
        // If the course doesn't have a room_spec, any room is fine.
        return course.getRoomSpec() == null || course.getRoomSpec().equals(room.getRoomType());
    }

    private int getScheduledClassesForCourse(Course course, List<Schedule> schedules) {
        int count = 0;
        for (Schedule schedule : schedules) {
            if (schedule.getCourse().getCommonId() != null &&
                    schedule.getCourse().getCommonId().equals(course.getCommonId())) {
                count++;
            }
        }
        return count;
    }

    private List<Schedule> getExistingSchedulesForCommonCourse(Course course, List<Schedule> schedules) {
        List<Schedule> matchingSchedules = new ArrayList<>();

        for (Schedule schedule : schedules) {
            // Check if the courses share the same common ID regardless of the program
            if (schedule.getCourse().getCommonId() != null &&
                    schedule.getCourse().getCommonId().equals(course.getCommonId())) {
                matchingSchedules.add(schedule);
            }
        }

        return matchingSchedules;
    }


    private boolean canUseExistingSchedule(Course course, Room room, TimeSlot timeSlot, List<Schedule> schedules) {
        return room.isAvailable(timeSlot) &&
                !instructorBusyAt(course.getInstructor(), timeSlot, schedules) &&
                !studentBusyAt(course, timeSlot, schedules) &&
                !timeSlotBusyAt(course, timeSlot, schedules) &&
                roomBelongsToDept(course, room);
    }

    private List<Schedule> getExistingSchedulesForCommonCourseByProgram(Course course, List<Schedule> schedules) {
        List<Schedule> existingSchedules = new ArrayList<>();

        for (Schedule schedule : schedules) {
            if (schedule.getCourse().getCommonId().equals(course.getCommonId()) && schedule.getCourse().getProgram().equals(course.getProgram())) {
                existingSchedules.add(schedule);
            }
        }

        return existingSchedules;
    }

}