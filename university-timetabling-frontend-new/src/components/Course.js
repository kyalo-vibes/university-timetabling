import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container } from 'react-bootstrap';

const Course = () => {
  const [courses, setCourses] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [programmes, setProgrammes] = useState([]);
  const [instructors, setInstructors] = useState([]);
  const [courseCode, setCourseCode] = useState('');
  const [courseName, setCourseName] = useState('');
  const [year, setYear] = useState('');
  const [semester, setSemester] = useState('');
  const [selectedProgrammeName, setSelectedProgrammeName] = useState('');
const [selectedDeptName, setSelectedDeptName] = useState('');
const [selectedInstructorName, setSelectedInstructorName] = useState('');


  // Fetch all courses
  const fetchCourses = () => {
    axios.get('http://localhost:8080/api/courses')
    .then(response => {
      setCourses(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Fetch all departments
  const fetchDepartments = () => {
    axios.get('http://localhost:8080/api/departments')
    .then(response => {
      setDepartments(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Fetch all programmes
  const fetchProgrammes = () => {
    axios.get('http://localhost:8080/api/programs')
    .then(response => {
      setProgrammes(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Fetch all instructors
  const fetchInstructors = () => {
    axios.get('http://localhost:8080/api/instructors')
    .then(response => {
      setInstructors(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Add a new course
  const addCourse = () => {
    const newCourse = {
      courseCode: courseCode,
      courseName: courseName,
      year: parseInt(year, 10),
      semester: parseInt(semester, 10),
      programmeName: selectedProgrammeName,
      deptName: selectedDeptName,
      instructorName: selectedInstructorName
    };

    axios.post('http://localhost:8080/api/courses', newCourse)
    .then(response => {
      alert('Course added');
      fetchCourses();
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  useEffect(() => {
    fetchCourses();
    fetchDepartments();
    fetchProgrammes();
    fetchInstructors();
  }, []);

  return (
    <Container>
      <h1>Courses</h1>

      <Form>
    <Form.Group>
        <Form.Label htmlFor="course_code">Course Code</Form.Label>
        <Form.Control type="text" id="course_code" value={courseCode} onChange={(e) => setCourseCode(e.target.value)} />
    </Form.Group>

    <Form.Group>
        <Form.Label htmlFor="course_name">Course Name</Form.Label>
        <Form.Control type="text" id="course_name" value={courseName} onChange={(e) => setCourseName(e.target.value)} />
    </Form.Group>

    <Form.Group>
        <Form.Label htmlFor="year">Year</Form.Label>
        <Form.Control type="number" id="year" value={year} onChange={(e) => setYear(e.target.value)} />
    </Form.Group>

    <Form.Group>
        <Form.Label htmlFor="semester">Semester</Form.Label>
        <Form.Control type="number" id="semester" value={semester} onChange={(e) => setSemester(e.target.value)} />
    </Form.Group>

    <Form.Group>
  <Form.Label htmlFor="programme">Programme</Form.Label>
  <Form.Control as="select" id="programme" value={selectedProgrammeName} onChange={(e) => setSelectedProgrammeName(e.target.value)}>
      {programmes.map(programme => <option key={programme.id} value={programme.programmeName}>{programme.programmeName}</option>)}
  </Form.Control>
</Form.Group>

<Form.Group>
  <Form.Label htmlFor="department">Department</Form.Label>
  <Form.Control as="select" id="department" value={selectedDeptName} onChange={(e) => setSelectedDeptName(e.target.value)}>
      {departments.map(department => <option key={department.id} value={department.deptName}>{department.deptName}</option>)}
  </Form.Control>
</Form.Group>

<Form.Group>
  <Form.Label htmlFor="instructor">Instructor</Form.Label>
  <Form.Control as="select" id="instructor" value={selectedInstructorName} onChange={(e) => setSelectedInstructorName(e.target.value)}>
      {instructors.map(instructor => <option key={instructor.id} value={instructor.firstName}>{instructor.firstName}</option>)}
  </Form.Control>
</Form.Group>

    <Button onClick={addCourse}>Add Course</Button>
</Form>


      <h2>Existing Courses</h2>
      {
        courses.map((course, index) => (
          <div key={index}>
            <h2>Existing Courses</h2>
{
  courses.map((course, index) => (
    <div key={index}>
      <h3>{course.courseName} ({course.courseCode})</h3>
      <p>Year: {course.year}</p>
      <p>Semester: {course.semester}</p>
      <p>Programme: {course.programmeName}</p>
      <p>Department: {course.deptName}</p>
      <p>Instructor: {course.instructorName}</p>
    </div>
  ))
}

            {/* ... */}
            {/* Provide Edit/Delete functionality here */}
          </div>
        ))
      }
    </Container>
  );
}

export default Course;
