import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, Form, Container } from "react-bootstrap";
import Layout from "../Layout";
const Course = () => {
  const [courses, setCourses] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [programmes, setProgrammes] = useState([]);
  const [instructors, setInstructors] = useState([]);
  const [courseCode, setCourseCode] = useState("");
  const [courseName, setCourseName] = useState("");
  const [year, setYear] = useState("");
  const [semester, setSemester] = useState("");
  const [selectedProgrammeName, setSelectedProgrammeName] = useState("");
  const [selectedDeptName, setSelectedDeptName] = useState("");
  const [selectedInstructorName, setSelectedInstructorName] = useState("");

  // Fetch all courses
  const fetchCourses = () => {
    axios
      .get("http://localhost:8080/api/courses")
      .then((response) => {
        console.log(response.data);
        setCourses(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all departments
  const fetchDepartments = () => {
    axios
      .get("http://localhost:8080/api/departments")
      .then((response) => {
        console.log(response.data);

        setDepartments(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all programmes
  const fetchProgrammes = () => {
    axios
      .get("http://localhost:8080/api/programs")
      .then((response) => {
        console.log(response.data);

        setProgrammes(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all instructors
  const fetchInstructors = () => {
    axios
      .get("http://localhost:8080/api/instructors")
      .then((response) => {
        setInstructors(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Add a new course
  const addCourse = () => {
    const newCourse = {
      courseCode: courseCode,
      courseName: courseName,
      year: parseInt(year, 10),
      semester: parseInt(semester, 10),
      programmeName: selectedProgrammeName,
      deptName: selectedDeptName,
      instructorName: selectedInstructorName,
    };

    axios
      .post("http://localhost:8080/api/courses", newCourse)
      .then((response) => {
        alert("Course added");
        fetchCourses();
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  useEffect(() => {
    fetchCourses();
    fetchDepartments();
    fetchProgrammes();
    fetchInstructors();
  }, []);

  return (
    <Layout>
      <main>
        <h1 className="font-bold text-3xl">Courses</h1>

        <form>
          <div>
            <label htmlFor="course_code" className="label">
              Course Code
            </label>
            <input
              className="input input-bordered w-full max-w-[18%]"
              type="text"
              id="course_code"
              value={courseCode}
              onChange={(e) => setCourseCode(e.target.value)}
            />
          </div>

          <div>
            <label htmlFor="course_name" className="label">
              Course Name
            </label>
            <input
              className="input input-bordered w-full max-w-[18%]"
              type="text"
              id="course_name"
              value={courseName}
              onChange={(e) => setCourseName(e.target.value)}
            />
          </div>

          <div>
            <label htmlFor="year" className="label">
              Year
            </label>
            <input
              className="input input-bordered w-full max-w-[18%]"
              type="number"
              id="year"
              value={year}
              onChange={(e) => setYear(e.target.value)}
            />
          </div>

          <div>
            <label htmlFor="semester" className="label">
              Semester
            </label>
            <input
              className="input input-bordered w-full max-w-[18%]"
              type="number"
              id="semester"
              value={semester}
              onChange={(e) => setSemester(e.target.value)}
            />
          </div>

          <div>
            <label htmlFor="programme" className="label">
              Programme
            </label>
            <select
              className="select select-info w-full max-w-[18%]"
              as="select"
              id="programme"
              value={selectedProgrammeName}
              onChange={(e) => setSelectedProgrammeName(e.target.value)}
            >
              {programmes.map((programme) => (
                <option key={programme.id} value={programme.programmeName}>
                  {programme.programmeName}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label htmlFor="department" className="label">
              Department
            </label>
            <select
              className="select select-info w-full max-w-[18%]"
              as="select"
              id="department"
              value={selectedDeptName}
              onChange={(e) => setSelectedDeptName(e.target.value)}
            >
              {departments.map((department) => (
                <option key={department.id} value={department.deptName}>
                  {department.deptName}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label htmlFor="instructor" className="label">
              Instructor
            </label>
            <select
              className="select select-info w-full max-w-[18%]"
              as="select"
              id="instructor"
              value={selectedInstructorName}
              onChange={(e) => setSelectedInstructorName(e.target.value)}
            >
              {instructors.map((instructor) => (
                <option key={instructor.id} value={instructor.firstName}>
                  {instructor.firstName}
                </option>
              ))}
            </select>
          </div>

          <button className="btn btn-primary" onClick={addCourse}>
            Add Course
          </button>
        </form>

        <h2>Existing Courses</h2>
        {courses.map((course, index) => (
          <div key={index}>
            <h2>Existing Courses</h2>
            {courses.map((course, index) => (
              <div key={index}>
                <h3>
                  {course.courseName} ({course.courseCode})
                </h3>
                <p>Year: {course.year}</p>
                <p>Semester: {course.semester}</p>
                <p>Programme: {course.programmeName}</p>
                <p>Department: {course.deptName}</p>
                <p>Instructor: {course.instructorName}</p>
              </div>
            ))}

            {/* ... */}
            {/* Provide Edit/Delete functionality here */}
          </div>
        ))}
      </main>
    </Layout>
  );
};

export default Course;
