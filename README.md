## Demo

https://github.com/kyalo-vibes/university-timetabling/assets/95200602/ddc2628b-e5c9-442a-810a-8609dde5c040

# University Timetabling System

The University Timetabling System is a software application designed to automate the process of generating timetables for university programs. It aims to efficiently schedule courses, instructors, and rooms while considering various constraints and requirements.

## Features

- **Scheduling for All Programs**: The system supports scheduling for all university programs, ensuring that each program has a dedicated timetable.
- **Scheduling for All Years**: If a program spans multiple years, the system generates separate timetables for each year, providing a comprehensive overview.
- **Semester-Specific Scheduling**: Scheduling is done specifically for the semester specified, enabling accurate planning and organization.
- **Inter-Faculty Course Scheduling**: The system can schedule courses from different faculties without collisions, allowing for interdisciplinary learning opportunities.
- **No Overlapping Lectures**: Lecturers are not assigned to teach two courses at the same time, ensuring an optimal teaching schedule.
- **Common Course and Faculty Course Conflict Avoidance**: Students are not assigned both a common course and a faculty-specific course at the same time, preventing conflicts in their schedules.
- **Room Availability Checking**: The system checks room availability before scheduling a course, preventing double-bookings and ensuring efficient utilization of resources.
- **Department-Based Room Assignment**: Rooms are assigned based on the department of the course, providing a suitable environment for each subject.
- **Scheduling per Semester**: Timetables are generated for each semester independently, accommodating different course offerings and faculty availability.
- **Scheduling Algorithm**: The system implements an iterative forward search algorithm with backtracking to generate timetables efficiently.
- **Optimization Techniques**: Heuristics, local search, and constraint propagation are implemented to optimize the scheduling algorithm and improve the quality of the timetables.

## Project Structure

The project follows a structured layout to ensure maintainability and modularity. The main components of the project are:

- **Domain**: Contains the domain models representing entities such as courses, departments, faculties, instructors, programs, rooms, schedules, sections, and timeslots.
- **Repository**: Provides repositories for each entity, responsible for data access and persistence.
- **Service**: Contains service classes that implement the business logic and handle the scheduling process.
- **Controller**: Provides REST API endpoints for interacting with the system, allowing clients to retrieve, create, update, and delete data.

## Scheduling Algorithm

The scheduling algorithm follows the following steps:

1. **Heuristic Function**: Define a heuristic function to determine the next best action in the scheduling process. For example, selecting the course with the fewest available timeslots or rooms.
2. **Evaluation Function**: Implement an evaluation function to assess the quality of a schedule based on constraints. Consider factors like constraint violations, instructor workload balance, and class distribution.
3. **Backtracking Function**: Implement a backtracking function to undo actions that lead to constraint violations. Use the evaluation function to choose the best action during backtracking.
4. **Scheduling Algorithm**: Implement an iterative forward search with backtracking. Start by assigning a course to a timeslot and room, then proceed to the next course using the heuristic function. If a constraint violation occurs, use the backtracking function to explore alternative paths.
5. **Local Search**: Apply local search techniques to improve the solution. For example, swapping timeslots between two courses and evaluating the new schedule using the evaluation function.
6. **ScheduleService Method**: Create a method in the ScheduleService class that generates a schedule for a given semester. Utilize the scheduling algorithm to produce the best possible schedule based on the specified constraints.




