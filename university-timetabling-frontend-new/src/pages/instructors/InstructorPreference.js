import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container } from 'react-bootstrap';

const InstructorPreference = () => {
    const [instructors, setInstructors] = useState([]);
    const [timeslots, setTimeslots] = useState([]);
    const [allPreferences, setAllPreferences] = useState([]); // New State
    const [selectedInstructorId, setSelectedInstructorId] = useState('');
    const [selectedTimeSlotId, setSelectedTimeSlotId] = useState('');
  
    const fetchInstructors = () => {
      axios.get('http://localhost:8080/api/instructors')
      .then(response => {
        setInstructors(response.data);
      })
      .catch(error => console.error(`Error: ${error}`));
    }
  
    const fetchTimeslots = () => {
      axios.get('http://localhost:8080/api/timeslots')
      .then(response => {
        setTimeslots(response.data);
      })
      .catch(error => console.error(`Error: ${error}`));
    }
  
    const fetchAllPreferences = () => {
        axios.get('http://localhost:8080/api/instructors/preferences')
        .then(response => {
          setAllPreferences(response.data);
        })
        .catch(error => console.error(`Error: ${error}`));
      }
  
    const addPreference = () => {
      axios.post(`http://localhost:8080/api/instructors/${selectedInstructorId}/preferences/${selectedTimeSlotId}`)
      .then(response => {
        alert('Preference added');
        fetchAllPreferences(); // Update this line
      })
      .catch(error => console.error(`Error: ${error}`));
    }
  
    useEffect(() => {
      fetchInstructors();
      fetchTimeslots();
      fetchAllPreferences(); // Fetch all preferences on initial load
    }, []);
  
    return (
      <Container>
        <h1>Instructor Preferences</h1>
  
        <Form onSubmit={(e) => {e.preventDefault(); addPreference();}}>
    <Form.Group>
        <Form.Label>Instructor</Form.Label>
        <Form.Control as="select" value={selectedInstructorId} onChange={(e) => {
            console.log("Selected instructor ID:", e.target.value);
            setSelectedInstructorId(e.target.value);
        }}>
        {instructors.map(instructor => <option key={instructor.id} value={instructor.id}>{instructor.firstName} {instructor.lastName}</option>)}
        </Form.Control>
    </Form.Group>
    <Form.Group>
        <Form.Label>Timeslot</Form.Label>
        <Form.Control as="select" id="timeslot" value={selectedTimeSlotId} onChange={(e) => setSelectedTimeSlotId(e.target.value)}>
            {timeslots.map(timeslot => 
                <option key={timeslot.id} value={timeslot.id}>
                {timeslot.day} {timeslot.startTime} - {timeslot.endTime}
                </option>
            )}
        </Form.Control>
    </Form.Group>
    <Button type="submit">Add Preference</Button>
</Form>

  
        {/* List of existing preferences */}
        <h2>Existing Preferences</h2>
        {
          allPreferences.map((instructorPreference, index) => (
            <div key={index}>
              <h3>{instructorPreference.instructorName}</h3>
              {instructorPreference.preferences.map((preference, i) => 
                <p key={i}>Timeslot: {preference.day} {preference.startTime}</p>
              )}
            </div>
          ))
        }
      </Container>
    );
  }
  
  export default InstructorPreference;
