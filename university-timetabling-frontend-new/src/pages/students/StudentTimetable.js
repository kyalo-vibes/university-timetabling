import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button } from 'react-bootstrap';
import { useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import Layout from "../../Layout/StudentDashboard"; // Adjust this import according to your project structure
import "../../styles/styles.css";

const StudentTimetable = () => {
  const { auth, setAuth } = useAuth();
  const navigate = useNavigate();
  const [timetable, setTimetable] = useState([]);
  const [timeslots, setTimeslots] = useState([]);


  // Fetch timetable of a student
  const fetchTimetable = () => {
    axios
      .get("http://localhost:8080/api/schedule/myTimetable", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setTimetable(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  const fetchTimeslots = () => {
    axios
      .get("http://localhost:8080/api/timeslots", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setTimeslots(response.data.map(timeslot => {
          const startHour = timeslot.startTime.slice(0, 5);
          const endHour = timeslot.endTime.slice(0, 5);
          return `${timeslot.day}: ${startHour} - ${endHour}`;
        }));
      })
      .catch((error) => console.error(`Error: ${error}`));
};

  // handle logout
  const logout = () => {
    setAuth({});
    localStorage.removeItem("user"); // Clear 'user' in localStorage
    navigate("/login");
  };

  useEffect(() => {
    fetchTimetable();
  }, []);

  console.log(timetable);

  
  useEffect(() => {
    fetchTimeslots();
  }, []);

  return (
    <Layout>
      <main>
        <section className="flex justify-between">
          <h1>Student Timetable</h1>
          <Button onClick={logout} variant="warning">
            Logout
          </Button>
        </section>
        <h2>Timetable</h2>
        <Table data={timetable} timeslots={timeslots} />
      </main>
    </Layout>
  );
};

const formatTimetableData = (scheduleData, uniqueTimeslots) => {
  console.log("Inside format timetable", scheduleData); // Add this line
  const objString = JSON.stringify(scheduleData);
  console.log(objString);
  const daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];

  // Initialize timetable
  let timetable = { 
    days: daysOfWeek,
    timeslots: uniqueTimeslots,
    schedule: {}
  };

  // Initialize schedule with empty arrays
  daysOfWeek.forEach(day => {
    timetable.schedule[day] = Array(uniqueTimeslots.length).fill(null);
  });

  // Make sure we have schedule data
  if (scheduleData) {
    console.log('scheduleData exists'); // Log if scheduleData exists
    
    // Check if timeSlots property exists on scheduleData
    if (scheduleData.timeSlots) {
      console.log('scheduleData.timeSlots exists'); // Log if scheduleData.timeSlots exists

      scheduleData.timeSlots.forEach((timeSlot, index) => {
        const day = timeSlot.split(' ')[0];
        const timeslotStripped = timeSlot.replace(day, '').trim(); 

        const timetableIndex = uniqueTimeslots.indexOf(timeslotStripped);

        if (timetableIndex !== -1) {
          timetable.schedule[day][timetableIndex] = {  
            courseCode: scheduleData.courseCodes[index],
            roomName: scheduleData.roomNames[index]
          };
        }
      });
    } else {
      console.log('scheduleData.timeSlots does not exist'); // Log if scheduleData.timeSlots does not exist
    }
  } else {
    console.log('No schedule data'); // Log if scheduleData does not exist
  }

  return timetable;
};








const Table = ({ data, timeslots }) => {
  console.log("Inside table component", data); // Add this line
  // Extract unique timeslots from the timeslots prop
  const uniqueTimeslots = [...new Set(timeslots.map(timeSlot => timeSlot.split(': ')[1]))];

// Check if data is an array and take the first element
const scheduleData = Array.isArray(data) ? data[0] : data;

  // As there's only one timetable, we don't need to map over data
  const timetable = formatTimetableData(scheduleData, uniqueTimeslots);

  return (
    <div className="timetable">
      <h3 className="timetable-header">Timetable for Student</h3>
      <p className="message">{data.message}</p>
      <table className="table-auto">
        <thead>
          <tr>
            <th className="px-4 py-2 border">Time Slot</th>
            {timetable.days.map(day => (
              <th key={day} className="px-4 py-2 border">{day}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {timetable.timeslots.map((timeslot, index) => (
            <tr key={timeslot}>
              <td className="border px-4 py-2">{timeslot}</td>
              {timetable.days.map(day => (
                <td key={day} className={`border px-4 py-2 ${timetable.schedule[day][index] ? '' : 'shaded'}`}>
                  {timetable.schedule[day][index] ? 
                    <>
                      <div>{timetable.schedule[day][index].courseCode}</div>
                      <div>{timetable.schedule[day][index].instructorNames}</div>
                      <div>{timetable.schedule[day][index].roomName}</div>
                    </> 
                    : ''}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default StudentTimetable;
