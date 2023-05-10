package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SchedulingService {

    private final CourseService courseService;
    private final InstructorService instructorService;
    private final RoomService roomService;
    private final TimeSlotService timeSlotService;
    private final ProgramService programService;


    @Autowired
    public SchedulingService(CourseService courseService,
                             InstructorService instructorService,
                             RoomService roomService,
                             TimeSlotService timeSlotService, ProgramService programService) {
        this.courseService = courseService;
        this.instructorService = instructorService;
        this.roomService = roomService;
        this.timeSlotService = timeSlotService;
        this.programService = programService;
    }

    public Map<String, Map<Integer, List<Schedule>>> generateScheduleForAllYearsAndPrograms(int semester) {
        List<Course> allCourses = courseService.getAllCourses();
        List<Program> allPrograms = programService.getAllPrograms();
        List<Instructor> instructors = instructorService.getAllInstructors();
        List<Room> rooms = roomService.getAllRooms();
        List<TimeSlot> timeSlots = timeSlotService.getAllTimeSlots();

        Map<String, Map<Integer, List<Schedule>>> schedulesByProgramAndYear = new HashMap<>();

        for (Program program : allPrograms) {
            Map<Integer, List<Schedule>> schedulesByYear = new HashMap<>();
            List<Course> programCourses = filterCoursesByProgram(allCourses, program);

            for (int year = 1; year <= 4; year++) {
                List<Course> filteredCourses = filterCoursesBySemesterAndYear(programCourses, semester, year);
                List<Schedule> generatedSchedule = new ArrayList<>();
                boolean result = scheduleCourses(0, filteredCourses, rooms, timeSlots, generatedSchedule);

                if (result) {
                    schedulesByYear.put(year, generatedSchedule);
                } else {
                    throw new RuntimeException("Failed to generate a schedule for " + program.getName() + " - Year " + year);
                }
            }
            schedulesByProgramAndYear.put(program.getName(), schedulesByYear);
        }

        return schedulesByProgramAndYear;
    }

    private List<Course> filterCoursesByProgram(List<Course> courses, Program program) {
        List<Course> filteredCourses = new ArrayList<>();

        for (Course course : courses) {
            if (course.getProgram().equals(program)) {
                filteredCourses.add(course);
            }
        }

        return filteredCourses;
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


    private List<Course> filterCoursesBySemesterAndYear(List<Course> courses, int semester, int year) {
        List<Course> filteredCourses = new ArrayList<>();

        for (Course course : courses) {
            if (course.getSemester() == semester && course.getYear() == year) {
                filteredCourses.add(course);
            }
        }

        return filteredCourses;
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
