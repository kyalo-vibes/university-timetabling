# Class Schedule API

This project provides an API for managing a class schedule at a university level. It's built with Java and uses Spring Boot for the backend. The scheduling algorithm uses iterative forward search with backtracking.

## Features

- Scheduling for all programs and all years in a program. If it's a 4-year program, 4 timetables are generated from 1st year up to 4th year.
- Scheduling is done for courses only for the specified semester.
- Able to schedule inter-faculty courses without collision.
- Ensures a lecturer is not given two courses to teach at the same time.
- Ensures a student does not have a common course and faculty course scheduled at the same time. Common courses start with a course code of 'CCS'.
- Checks room availability and department relevance before scheduling.
- View schedules by instructor.
- View schedules for a specific program, year, and semester.
- Each schedule includes the course code, instructor name, room name, and time slot.

## Hard Constraints

- Room availability.
- Instructor availability.

## Soft Constraints

- Preferred time slot.

## Optimization Ideas

- Use a heuristic to choose the most promising course to schedule next, rather than always going in order.
- Try to schedule the most difficult courses first (those with the fewest available rooms or time slots).
- Implement some form of local search or constraint propagation to prune the search space.

## Project Structure

The project is organized as follows:
com.kyalo.universitytimetabling
│
├── domain
│   ├── Course.java
│   ├── Department.java
│   ├── Faculty.java
│   ├── Instructor.java
│   ├── Program.java
│   ├── Room.java
│   ├── Schedule.java
│   ├── Section.java
│   └── TimeSlot.java
│
├── repository
│   ├── CourseRepository.java
│   ├── DepartmentRepository.java
│   ├── FacultyRepository.java
│   ├── InstructorRepository.java
│   ├── ProgramRepository.java
│   ├── RoomRepository.java
│   ├── ScheduleRepository.java
│   ├── SectionRepository.java
│   └── TimeSlotRepository.java
│
├── service
│   ├── CourseService.java
│   ├── DepartmentService.java
│   ├── FacultyService.java
│   ├── InstructorService.java
│   ├── ProgramService.java
│   ├── RoomService.java
│   ├── ScheduleService.java
│   ├── SectionService.java
│   └── TimeSlotService.java
│
└── controller
    ├── CourseController.java
    ├── DepartmentController.java
    ├── FacultyController.java
    ├── InstructorController.java
    ├── ProgramController.java
    ├── RoomController.java
    ├── ScheduleController.java
    ├── SectionController.java
    └── TimeSlotController.java



## Getting Started

### Prerequisites

- Java 11+
- Maven
- MySQL

### Installation

1. Clone the repo:
    ```
    git clone https://github.com/kyalo-vibes/university-timetabling
    ```

2. Build the project:
    ```
    mvn clean install
    ```

3. Configure your database connection in `src/main/resources/application.properties`.

4. Run the project:
    ```
    mvn spring-boot:run
    ```

The server will start, and you can access the API at `http://localhost:8080`.

## API Documentation

You can find the API documentation at `http://localhost:8080/swagger-ui.html` when the server is running.

## Set up Demo

