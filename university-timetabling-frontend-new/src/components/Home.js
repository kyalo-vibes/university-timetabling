import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container } from 'react-bootstrap';

const Home = () => {
  const [semester, setSemester] = useState(1);
  const [timetables, setTimetables] = useState([]);

  // Function to generate timetable
  const generateTimetable = () => {
    axios.post('http://localhost:8080/api/generate', { semester: semester })
    .then(response => {
      alert('Timetable generated');
      fetchTimetables();
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Function to fetch all timetables
  const fetchTimetables = () => {
    axios.get('http://localhost:8080/api/timetables')
    .then(response => {
      setTimetables(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Fetch all timetables on initial render
  useEffect(() => {
    fetchTimetables();
  }, []);

  return (
    <Container>
      <h1>University Timetabling System</h1>

      <Form>
        <Form.Group>
          <Form.Label>Semester</Form.Label>
          <Form.Control 
            type="number" 
            value={semester} 
            onChange={(e) => setSemester(e.target.value)} 
            min={1} 
            max={2} 
          />
        </Form.Group>
        <Button onClick={generateTimetable}>Generate Timetable</Button>
      </Form>

      <h2>Timetables</h2>
      {
        timetables.map((timetable, index) => (
          <div key={index}>
            <h3>Semester {timetable.semester}</h3>
            <pre>{JSON.stringify(timetable.data, null, 2)}</pre>
          </div>
        ))
      }
    </Container>
  );
}

export default Home;
