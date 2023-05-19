import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container } from 'react-bootstrap';

const Department = () => {
  const [departments, setDepartments] = useState([]);
  const [faculties, setFaculties] = useState([]);
  const [deptCode, setDeptCode] = useState('');
  const [deptName, setDeptName] = useState('');
  const [selectedFacultyName, setSelectedFacultyName] = useState('');

  // Fetch all departments
  const fetchDepartments = () => {
    axios.get('http://localhost:8080/api/departments')
    .then(response => {
      setDepartments(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Fetch all faculties
  const fetchFaculties = () => {
    axios.get('http://localhost:8080/api/faculties')
      .then(response => {
        setFaculties(response.data);
      })
      .catch(error => console.error(`Error: ${error}`));
  }


    // Add a new department
  const addDepartment = () => {
    const newDepartment = {
      deptCode: deptCode,
      deptName: deptName,
      facultyName: selectedFacultyName
    };
  
    axios.post('http://localhost:8080/api/departments', newDepartment)
    .then(response => {
      alert('Department added');
      fetchDepartments();
    })
    .catch(error => console.error(`Error: ${error}`));
  }


  useEffect(() => {
    fetchDepartments();
    fetchFaculties();
  }, []);

  return (
    <Container>
      <h1>Departments</h1>

      <Form onSubmit={(e) => {e.preventDefault(); addDepartment();}}>
  <Form.Group>
    <Form.Label>Department Code</Form.Label>
    <Form.Control type="text" value={deptCode} onChange={(e) => setDeptCode(e.target.value)} />
  </Form.Group>
  <Form.Group>
    <Form.Label>Department Name</Form.Label>
    <Form.Control type="text" value={deptName} onChange={(e) => setDeptName(e.target.value)} />
  </Form.Group>
  <Form.Group>
    <Form.Label>Faculty</Form.Label>
    <Form.Control as="select" value={selectedFacultyName} onChange={(e) => setSelectedFacultyName(e.target.value)}>
      {faculties.map(faculty => <option key={faculty.id} value={faculty.facultyName}>{faculty.facultyName}</option>)}
    </Form.Control>
  </Form.Group>
  <Button type="submit">Add Department</Button>
</Form>


      <h2>Existing Departments</h2>
      {
        departments.map((department, index) => (
          <div key={index}>
            <h3>{department.deptCode}</h3>
            <p>Department Name: {department.deptName}</p>
            {/* Provide Edit/Delete functionality here */}
          </div>
        ))
      }
    </Container>
  );
}


export default Department;
