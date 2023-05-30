import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, Form, Container } from "react-bootstrap";
import Layout from "../Layout/DashboardLayout";
import { useContext } from "react";
import AuthContext from "../context/AuthProvider";
import { useNavigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";
import "../styles/styles.css";


const Home = () => {
  const [semester, setSemester] = useState(1);
  const [timetables, setTimetables] = useState([]);
  const [loading, setLoading] = useState(false); // New state variable for loading
  const [isCompleted, setIsCompleted] = useState(false); // New state variable for completion
  const { setAuth } = useContext(AuthContext);
  const { auth } = useAuth();
  const navigate = useNavigate();

  // Function to generate timetable
  const generateTimetable = (event) => {
    event.preventDefault();
    setLoading(true); 
    axios
    .post(
      `http://localhost:8080/api/schedule/generate?semester=${semester}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      }
    )
    .then((response) => {
      let intervalId = setInterval(() => {
        axios
          .get(
            `http://localhost:8080/api/schedule/status?semester=${semester}`,
            {
              headers: {
                Authorization: `Bearer ${auth.accessToken}`,
              },
            }
          )
          .then((response) => {
            if (response.data === 'COMPLETED') {
              clearInterval(intervalId);
              setLoading(false); 
              setIsCompleted(true); 
              // Use setTimeout to create a small delay before the alert
              window.setTimeout(() => alert("Timetables generated"), 50);
              fetchTimetables(); 
            }
          })
          .catch((error) => console.error(`Error: ${error}`));
      }, 5000);
    })
    .catch((error) => {
      console.error(`Error: ${error}`);
      setLoading(false);
    });
};



  // Function to fetch all timetables
  const fetchTimetables = () => {
    axios
      .get("http://localhost:8080/api/schedule/timetables", {
        headers: {
          Authorization: `Bearer ${auth.accessToken}`,
        },
      })
      .then((response) => {
        setTimetables(response.data);
      })
      .catch((error) => console.error(`Error: ${error}`));
  };

  // Fetch all timetables on initial render
  useEffect(() => {
    if (!loading && isCompleted) {
      const timer = setTimeout(() => setIsCompleted(false), 2000);
      return () => clearTimeout(timer);
    }
  }, [loading, isCompleted]);

  useEffect(() => {
    fetchTimetables();
  }, []);

  // handle logout
async function logout() {
  setAuth({});
  localStorage.removeItem("user");  // Clear 'user' in localStorage
  navigate("/login");
}

  return (
    <Layout>
      <main>
      {loading || isCompleted ? (
      <div className="loading-overlay">
        <div className={`spinner ${isCompleted ? 'success' : 'spinning'}`}>
          {isCompleted && <div className="success-message">✔︎</div>}
        </div>
        <p className="text">
          {isCompleted ? 'Successfully generated timetables!' : 'Generating timetables...'}
        </p>
      </div>
    ) : null}

        <h1>University Timetabling System</h1>
        <button onClick={logout} className="btn btn-warning">
          Logout
        </button>

        <form>
          <div>
            <label>Semester</label>
            <select className="select select-accent w-full max-w-xs">
              <option disabled selected>
                Select the semester
              </option>
              <option>1</option>
              <option>2</option>
            </select>
          </div>
          <button className="btn btn-accent" onClick={(event) => generateTimetable(event)}>
  Generate Timetable
</button>
        </form>

        <h2>Timetables</h2>
        {timetables.map((timetable, index) => (
          <div key={index}>
            <h3>Semester {timetable.semester}</h3>
            <pre>{JSON.stringify(timetable.data, null, 2)}</pre>
          </div>
        ))}
      </main>
    </Layout>
  );
};

export default Home;
