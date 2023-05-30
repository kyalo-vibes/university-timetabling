CREATE DATABASE university_timetabling;
USE university_timetabling;

CREATE TABLE faculties (
  faculty_id INT AUTO_INCREMENT PRIMARY KEY,
  faculty_code VARCHAR(10),
  faculty_name VARCHAR(255)
);


CREATE TABLE departments (
  dept_id INT AUTO_INCREMENT PRIMARY KEY,
  dept_code VARCHAR(10),
  dept_name VARCHAR(255),
  faculty_id INT,
  FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)
);

CREATE TABLE rooms (
  room_id INT AUTO_INCREMENT PRIMARY KEY,
  room_name VARCHAR(255),
  room_capacity INT,
  room_type VARCHAR(255),
  is_available BOOL,
  dept_id int,
  FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);

CREATE TABLE room_department (
    room_id INT NOT NULL,
    dept_id INT NOT NULL,
    PRIMARY KEY (room_id, dept_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id),
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);

CREATE TABLE programmes (
  programme_id INT AUTO_INCREMENT PRIMARY KEY,
  programme_code VARCHAR(10),
  programme_name VARCHAR(255),
  faculty_id INT,
  FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)
);

CREATE TABLE program_enrollments (
  enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
  program_id INT NOT NULL,
  enrollment_year INT NOT NULL,
  enrollment_number INT NOT NULL,
  FOREIGN KEY (program_id) REFERENCES programmes (programme_id)
);


CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(255),
  user_type VARCHAR(255)
);

CREATE TABLE students (
  id INT PRIMARY KEY AUTO_INCREMENT,
  year INT NOT NULL,
  user_id INT,
  program_id INT,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (program_id) REFERENCES programmes(programme_id)
);

CREATE TABLE instructors (
  instructor_id INT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  dept_id INT NOT NULL,
  user_id INT,
  FOREIGN KEY (dept_id) REFERENCES departments(dept_id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE courses (
  course_id INT AUTO_INCREMENT PRIMARY KEY,
  course_code VARCHAR(10),
  course_name VARCHAR(255),
  year INT,
  semester INT,
  room_spec VARCHAR(255),
  programme_id INT,
  dept_id INT,
  instructor_id INT,
  common_id INT,
  FOREIGN KEY (programme_id) REFERENCES programmes(programme_id),
  FOREIGN KEY (dept_id) REFERENCES departments(dept_id),
  FOREIGN KEY (instructor_id) REFERENCES instructors(instructor_id)
);


CREATE TABLE time_slots (
  id INT PRIMARY KEY AUTO_INCREMENT,
  day VARCHAR(255) NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL
);

CREATE TABLE sections(
   section_id                INTEGER  NOT NULL PRIMARY KEY  AUTO_INCREMENT
  ,number_of_classes INTEGER  NOT NULL
  ,course_id         INTEGER  NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);

CREATE TABLE instructor_preferences (
    instructor_id INT,
    timeslot_id INT,
    PRIMARY KEY (instructor_id, timeslot_id),
    FOREIGN KEY (instructor_id) REFERENCES instructors(instructor_id),
    FOREIGN KEY (timeslot_id) REFERENCES time_slots(id)
);

CREATE TABLE schedule (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT,
    room_id INT,
    time_slot_id INT,
    section_id INT,
    FOREIGN KEY (course_id) REFERENCES courses (course_id),
    FOREIGN KEY (room_id) REFERENCES rooms (room_id),
    FOREIGN KEY (time_slot_id) REFERENCES time_slots (id),
    FOREIGN KEY (section_id) REFERENCES sections (section_id)
);

CREATE TABLE schedule_status (
    id INT AUTO_INCREMENT PRIMARY KEY,
    semester INT,
    status VARCHAR(20)
);
create table rooms_occupied_time_slots (
       room_room_id int not null,
        occupied_time_slots_id int not null
);
create table schedule_results (
       id int not null,
        message varchar(255),
        schedule_result_id int,
        primary key (id)
    );
create table schedule_results_seq (
       next_val int
    );
create table schedule_result_course_codes (
       schedule_result_id int not null,
        course_codes varchar(255)
    );
create table schedule_result_instructor_names (
       schedule_result_id int not null,
        instructor_names varchar(255)
    );
create table schedule_result_room_names (
       schedule_result_id int not null,
        room_names varchar(255)
    );
create table schedule_result_time_slots (
       schedule_result_id int not null,
        time_slots varchar(255)
    ); 

-- Add foreign key constraints to the rooms_occupied_time_slots table
ALTER TABLE rooms_occupied_time_slots
    ADD CONSTRAINT FKbuauac2j381gvqf5l9jfpf2q6
    FOREIGN KEY (room_room_id)
    REFERENCES rooms (room_id);

ALTER TABLE rooms_occupied_time_slots
    ADD CONSTRAINT FKqyxfeh2u0ix5adr3jo57dasti
    FOREIGN KEY (occupied_time_slots_id)
    REFERENCES time_slots (id);

-- Add foreign key constraints to the schedule_results table
ALTER TABLE schedule_results
    ADD CONSTRAINT FKklltntgdt9pb2i5s3cmd8b0tq
    FOREIGN KEY (schedule_result_id)
    REFERENCES schedule_results (id);

-- Add foreign key constraints to the schedule_result_course_codes table
ALTER TABLE schedule_result_course_codes
    ADD CONSTRAINT FKklltntgdt9pb2i3s3cmd8b0tq
    FOREIGN KEY (schedule_result_id)
    REFERENCES schedule_results (id);

-- Add foreign key constraints to the schedule_result_instructor_names table
ALTER TABLE schedule_result_instructor_names
    ADD CONSTRAINT FKjwbv8exa4mgun03k15od9iuba
    FOREIGN KEY (schedule_result_id)
    REFERENCES schedule_results (id);

-- Add foreign key constraints to the schedule_result_room_names table
ALTER TABLE schedule_result_room_names
    ADD CONSTRAINT FKfleegajlhqgxfr6o4ty0r2gn
    FOREIGN KEY (schedule_result_id)
    REFERENCES schedule_results (id);

-- Add foreign key constraints to the schedule_result_time_slots table
ALTER TABLE schedule_result_time_slots
    ADD CONSTRAINT FK771x4mbm9byeq1v7ffgcfru2h
    FOREIGN KEY (schedule_result_id)
    REFERENCES schedule_results (id);

-- DROP DATABASE university_timetabling;

-- GRANT ALL PRIVILEGES ON university_timetabling.* TO 'root'@'localhost'; 


