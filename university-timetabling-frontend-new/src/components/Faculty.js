import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container } from 'react-bootstrap';

const Faculty = () => {
  const [faculties, setFaculties] = useState([]);
  const [facultyCode, setFacultyCode] = useState('');
  const [facultyName, setFacultyName] = useState('');

  // Fetch all faculties
  const fetchFaculties = () => {
    // assuming '/api/faculties' is the endpoint to get all faculties
    axios.get('http://localhost:8080/api/faculties')
    .then(response => {
      setFaculties(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Add a new faculty
const addFaculty = () => {
  const newFaculty = {
    facultyCode: facultyCode,
    facultyName: facultyName
  };

  axios.post('http://localhost:8080/api/faculties', newFaculty, {
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => {
    alert('Faculty added');
    fetchFaculties();
  })
  .catch(error => console.error(`Error: ${error}`));
}


  useEffect(() => {
    fetchFaculties();
  }, []);

  return (
    <Container>
      <h1>Faculties</h1>

      <Form onSubmit={(e) => {e.preventDefault(); addFaculty();}}>
  <Form.Group>
    <Form.Label>Faculty Code</Form.Label>
    <Form.Control type="text" value={facultyCode} onChange={(e) => setFacultyCode(e.target.value)} />
  </Form.Group>
  <Form.Group>
    <Form.Label>Faculty Name</Form.Label>
    <Form.Control type="text" value={facultyName} onChange={(e) => setFacultyName(e.target.value)} />
  </Form.Group>
  <Button type="submit">Add Faculty</Button>
</Form>


      <h2>Existing Faculties</h2>
      {
        faculties.map((faculty, index) => (
          <div key={index}>
            <h3>{faculty.facultyCode}</h3>
            <p>Faculty Name: {faculty.facultyName}</p>
            {/* Provide Edit/Delete functionality here */}
          </div>
        ))
      }
    </Container>
  );
}


export default Faculty;
