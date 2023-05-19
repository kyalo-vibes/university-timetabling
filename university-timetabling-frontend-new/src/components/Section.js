import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container } from 'react-bootstrap';

const Section = () => {
  const [sections, setSections] = useState([]);
  const [courses, setCourses] = useState([]);
  const [numberOfClasses, setNumberOfClasses] = useState('');
  const [selectedCourseName, setSelectedCourseName] = useState('');

  // Fetch all sections
  const fetchSections = () => {
    axios.get('http://localhost:8080/api/sections')
    .then(response => {
      setSections(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Fetch all courses
  const fetchCourses = () => {
    axios.get('http://localhost:8080/api/courses')
    .then(response => {
      setCourses(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Add a new section
  const addSection = () => {
    const newSection = {
      numberOfClasses: numberOfClasses,
      courseName: selectedCourseName
    };

    axios.post('http://localhost:8080/api/sections', newSection)
    .then(response => {
      alert('Section added');
      fetchSections();
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  useEffect(() => {
    fetchSections();
    fetchCourses();
  }, []);

  return (
    <Container>
      <h1>Sections</h1>

      <Form onSubmit={(e) => {e.preventDefault(); addSection();}}>
        <Form.Group>
          <Form.Label>Number of Classes</Form.Label>
          <Form.Control type="number" value={numberOfClasses} onChange={(e) => setNumberOfClasses(e.target.value)} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Course</Form.Label>
          <Form.Control as="select" value={selectedCourseName} onChange={(e) => setSelectedCourseName(e.target.value)}>
            {courses.map(course => <option key={course.id} value={course.courseName}>{course.courseName}</option>)}
          </Form.Control>
        </Form.Group>
        <Button type="submit">Add Section</Button>
      </Form>

      <h2>Existing Sections</h2>
      {
        sections.map((section, index) => (
          <div key={index}>
            <h3>Section ID: {section.id}</h3>
            <p>Number of Classes: {section.numberOfClasses}</p>
            <p>Course: {section.courseName}</p>
            {/* Provide Edit/Delete functionality here */}
          </div>
        ))
      }
    </Container>
  );
}

export default Section;
