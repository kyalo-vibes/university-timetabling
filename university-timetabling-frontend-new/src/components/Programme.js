import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container } from 'react-bootstrap';

const Programme = () => {
  const [programmes, setProgrammes] = useState([]);
  const [faculties, setFaculties] = useState([]);
  const [programmeCode, setProgrammeCode] = useState('');
  const [programmeName, setProgrammeName] = useState('');
  const [selectedFacultyName, setSelectedFacultyName] = useState('');

  // Fetch all programmes
  const fetchProgrammes = () => {
    axios.get('http://localhost:8080/api/programs')
    .then(response => {
      setProgrammes(response.data);
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

  // Add a new programme
  const addProgramme = (e) => {
    e.preventDefault();
    
    const newProgramme = {
      programmeCode: programmeCode,
      programmeName: programmeName,
      facultyName: selectedFacultyName
    };

    axios.post('http://localhost:8080/api/programs', newProgramme)
    .then(response => {
      alert('Programme added');
      fetchProgrammes();
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  useEffect(() => {
    fetchProgrammes();
    fetchFaculties();
  }, []);

  return (
    <Container>
      <h1>Programmes</h1>

      <Form onSubmit={addProgramme}>
        <Form.Group>
          <Form.Label>Programme Code</Form.Label>
          <Form.Control type="text" value={programmeCode} onChange={(e) => setProgrammeCode(e.target.value)} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Programme Name</Form.Label>
          <Form.Control type="text" value={programmeName} onChange={(e) => setProgrammeName(e.target.value)} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Faculty</Form.Label>
          <Form.Control as="select" value={selectedFacultyName} onChange={(e) => setSelectedFacultyName(e.target.value)}>
            {faculties.map(faculty => <option key={faculty.id} value={faculty.facultyName}>{faculty.facultyName}</option>)}
          </Form.Control>
        </Form.Group>
        <Button type="submit">Add Programme</Button>
      </Form>

      <h2>Existing Programmes</h2>
      {
        programmes.map((programme, index) => (
          <div key={index}>
            <h3>{programme.code}</h3>
            <p>Programme Name: {programme.programmeName}</p>
            {/* Provide Edit/Delete functionality here */}
          </div>
        ))
      }
    </Container>
  );
}

export default Programme;
