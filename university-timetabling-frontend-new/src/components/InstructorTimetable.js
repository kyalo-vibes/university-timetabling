import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, Form, FormGroup, Container } from "react-bootstrap";

const InstructorTimetable = () => {
  const [instructorId, setInstructorId] = useState("");
  const [timetable, setTimetable] = useState([]);

  // Fetch timetable of an instructor
  const fetchTimetable = () => {
    axios
      .get(`/api/timetables/instructor/${instructorId}`)
      .then((response) => {
        setTimetable(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  return (
    <Container>
      <h1>Instructor Timetable</h1>

      <Form inline>
        <FormGroup>
          <label for="instructorId" className="mr-sm-2">
            Instructor ID
          </label>
          <input
            type="text"
            id="instructorId"
            value={instructorId}
            onChange={(e) => setInstructorId(e.target.value)}
          />
        </FormGroup>
        <Button onClick={fetchTimetable}>Fetch Timetable</Button>
      </Form>

      <h2>Timetable</h2>
      {timetable.map((entry, index) => (
        <div key={index}>
          {/* Display the timetable entries here. */}
          {/* For example, you might have: */}
          <p>
            {entry.day} {entry.start_time} - {entry.end_time}:{" "}
            {entry.course_name} ({entry.room_name})
          </p>
        </div>
      ))}
    </Container>
  );
};

export default InstructorTimetable;
