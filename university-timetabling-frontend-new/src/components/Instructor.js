import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container } from 'react-bootstrap';

const Instructor = () => {
  const [instructors, setInstructors] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [selectedDeptName, setSelectedDeptName] = useState('');

  // Fetch all instructors
  const fetchInstructors = () => {
    axios.get('http://localhost:8080/api/instructors')
    .then(response => {
      setInstructors(response.data);
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

  // Add a new instructor
  const addInstructor = () => {
    const newInstructor = {
      firstName: firstName,
      lastName: lastName,
      deptName: selectedDeptName
    };

    axios.post('http://localhost:8080/api/instructors', newInstructor)
    .then(response => {
      alert('Instructor added');
      fetchInstructors();
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  useEffect(() => {
    fetchInstructors();
    fetchDepartments();
  }, []);

  return (
    <Container>
      <h1>Instructors</h1>

      <Form onSubmit={(e) => {e.preventDefault(); addInstructor();}}>
        <Form.Group>
          <Form.Label>First Name</Form.Label>
          <Form.Control type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Last Name</Form.Label>
          <Form.Control type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Department</Form.Label>
          <Form.Control as="select" value={selectedDeptName} onChange={(e) => setSelectedDeptName(e.target.value)}>
            {departments.map(department => <option key={department.id} value={department.deptName}>{department.deptName}</option>)}
          </Form.Control>
        </Form.Group>
        <Button type="submit">Add Instructor</Button>
      </Form>

      <h2>Existing Instructors</h2>
      {
        instructors.map((instructor, index) => (
          <div key={index}>
            <h3>{instructor.firstName} {' '} {instructor.lastName}</h3>
            <p>Department Name: {instructor.deptName}</p>
            {/* Provide Edit/Delete functionality here */}
          </div>
        ))
      }
    </Container>
  );
}

export default Instructor;
