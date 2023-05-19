import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Form, Container} from 'react-bootstrap';

const TimeSlot = () => {
  const [timeSlots, setTimeSlots] = useState([]);
  const [day, setDay] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');

  // Fetch all time slots
  const fetchTimeSlots = () => {
    // assuming '/api/timeslots' is the endpoint to get all time slots
    axios.get('http://localhost:8080/api/timeslots')
    .then(response => {
      setTimeSlots(response.data);
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  // Add a new time slot
  const addTimeSlot = () => {
    const newTimeSlot = {
      day: day,
      startTime: startTime,
      endTime: endTime
    };

    axios.post('http://localhost:8080/api/timeslots', newTimeSlot)
    .then(response => {
      alert('Time slot added');
      fetchTimeSlots();
    })
    .catch(error => console.error(`Error: ${error}`));
  }

  useEffect(() => {
    fetchTimeSlots();
  }, []);

  return (
    <Container>
      <h1>Time Slots</h1>

      <Form onSubmit={(e) => {e.preventDefault(); addTimeSlot();}}>
  <Form.Group>
    <Form.Label for="day">Day</Form.Label>
    <Form.Control type="text" id="day" value={day} onChange={(e) => setDay(e.target.value)} />
  </Form.Group>
  <Form.Group>
    <Form.Label for="startTime">Start Time</Form.Label>
    <Form.Control type="time" id="startTime" value={startTime} onChange={(e) => setStartTime(e.target.value)} />
  </Form.Group>
  <Form.Group>
    <Form.Label for="endTime">End Time</Form.Label>
    <Form.Control type="time" id="endTime" value={endTime} onChange={(e) => setEndTime(e.target.value)} />
  </Form.Group>
  <Button type="submit">Add Time Slot</Button>
</Form>


      <h2>Existing Time Slots</h2>
      {
        timeSlots.map((timeSlot, index) => (
          <div key={index}>
            <h3>{timeSlot.day}</h3>
            <p>Start Time: {timeSlot.startTime}</p>
            <p>End Time: {timeSlot.endTime}</p>
            {/* Provide Edit/Delete functionality here */}
          </div>
        ))
      }
    </Container>
  );
}

export default TimeSlot;
