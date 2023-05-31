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

  // handle logout
  const logout = () => {
    setAuth({});
    localStorage.removeItem("user"); // Clear 'user' in localStorage
    navigate("/login");
  };

  useEffect(() => {
    fetchTimetable();
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
        <Table data={timetable} />
      </main>
    </Layout>
  );
};

const Table = ({ data }) => {
  return (
    <table className="table-auto">
      <thead>
        <tr>
          <th className="px-4 py-2">Course Code</th>
          <th className="px-4 py-2">Time Slot</th>
          <th className="px-4 py-2">Instructor Name</th>
          <th className="px-4 py-2">Room Name</th>
        </tr>
      </thead>
      <tbody>
        {Object.keys(data).map((program) =>
          data[program].courseCodes.map((course, index) => (
            <tr key={`${program}-${index}`}>
              <td className="border px-4 py-2">{course}</td>
              <td className="border px-4 py-2">{data[program].timeSlots[index]}</td>
              <td className="border px-4 py-2">{data[program].instructorNames[index]}</td>
              <td className="border px-4 py-2">{data[program].roomNames[index]}</td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
};

export default StudentTimetable;
